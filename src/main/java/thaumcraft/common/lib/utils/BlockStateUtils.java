package thaumcraft.common.lib.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;

public class BlockStateUtils {

    public static Direction getFacing(BlockState state) {
        // Try common property names first; fall back to NORTH
        for (Property<?> property : state.getProperties()) {
            String name = property.getName();
            if ("facing".equals(name) || "horizontal_facing".equals(name)) {
                Object value = state.getValue(property);
                if (value instanceof Direction) {
                    return (Direction) value;
                }
            }
        }
        return Direction.NORTH;
    }

    // Metadata no longer exists in 1.16; keep a helper for legacy callers
    public static Direction getFacing(int meta) {
        int idx = meta & 0x7;
        try {
            return Direction.from3DDataValue(idx);
        } catch (Throwable t) {
            return Direction.NORTH;
        }
    }

    public static boolean isEnabled(BlockState state) {
        // Prefer a boolean property named "enabled" when present; otherwise assume enabled
        for (Property<?> property : state.getProperties()) {
            if ("enabled".equals(property.getName())) {
                Object value = state.getValue(property);
                if (value instanceof Boolean) {
                    return (Boolean) value;
                }
            }
        }
        return true;
    }

    public static boolean isEnabled(int meta) {
        // Legacy behavior: meta bit 0x8 indicated disabled; invert that as a best-effort
        return (meta & 0x8) != 0x8;
    }

    public static Property<?> getPropertyByName(BlockState blockState, String propertyName) {
        for (Property<?> property : blockState.getProperties()) {
            if (property.getName().equals(propertyName)) {
                return property;
            }
        }
        return null;
    }

    public static boolean isValidPropertyName(BlockState blockState, String propertyName) {
        return getPropertyByName(blockState, propertyName) != null;
    }

    public static <T extends Comparable<T>> T getPropertyValueByName(BlockState blockState, Property<T> property, String valueName) {
        for (T value : property.getPossibleValues()) {
            if (value.toString().equals(valueName)) {
                return value;
            }
        }
        return null;
    }

    @SafeVarargs
    public static ImmutableSet<BlockState> getValidStatesForProperties(BlockState baseState, Property<? extends Comparable<?>>... properties) {
        if (properties == null) {
            return null;
        }
        Set<BlockState> validStates = Sets.newHashSet();
        PropertyIndexer propertyIndexer = new PropertyIndexer(properties);
        do {
            BlockState currentState = baseState;
            for (Property<? extends Comparable<?>> property : properties) {
                IndexedProperty<?> indexedProperty = propertyIndexer.getIndexedProperty(property);
                currentState = setValueUnchecked(currentState, property, indexedProperty.getCurrentValue());
            }
            validStates.add(currentState);
        } while (propertyIndexer.increment());
        return ImmutableSet.copyOf((Collection<BlockState>) validStates);
    }

    private static <T extends Comparable<T>> BlockState setValueUnchecked(BlockState state, Property<T> property, Object value) {
        @SuppressWarnings("unchecked")
        T cast = (T) value;
        return state.setValue(property, cast);
    }

    private static class PropertyIndexer {
        private final HashMap<Property<?>, IndexedProperty<?>> indexedProperties;
        private final Property<?> finalProperty;

        @SafeVarargs
        private PropertyIndexer(Property<? extends Comparable<?>>... properties) {
            indexedProperties = new HashMap<>();
            finalProperty = properties[properties.length - 1];
            IndexedProperty<?> previousIndexedProperty = null;
            for (Property<? extends Comparable<?>> property : properties) {
                IndexedProperty<?> indexedProperty = new IndexedProperty<>(property);
                if (previousIndexedProperty != null) {
                    indexedProperty.parent = previousIndexedProperty;
                    previousIndexedProperty.child = indexedProperty;
                }
                indexedProperties.put(property, indexedProperty);
                previousIndexedProperty = indexedProperty;
            }
        }

        public boolean increment() {
            return indexedProperties.get(finalProperty).increment();
        }

        public <T extends Comparable<T>> IndexedProperty<T> getIndexedProperty(Property<T> property) {
            @SuppressWarnings("unchecked")
            IndexedProperty<T> res = (IndexedProperty<T>) indexedProperties.get(property);
            return res;
        }
    }

    private static class IndexedProperty<T extends Comparable<T>> {
        private final ArrayList<T> validValues;
        private final int maxCount;
        private int counter;
        private IndexedProperty<?> parent;
        private IndexedProperty<?> child;

        private IndexedProperty(Property<T> property) {
            validValues = new ArrayList<>();
            validValues.addAll(property.getPossibleValues());
            maxCount = validValues.size() - 1;
        }

        public boolean increment() {
            if (counter < maxCount) {
                ++counter;
                return true;
            }
            if (hasParent()) {
                resetSelfAndChildren();
                return parent.increment();
            }
            return false;
        }

        public void resetSelfAndChildren() {
            counter = 0;
            if (hasChild()) {
                child.resetSelfAndChildren();
            }
        }

        public boolean hasParent() {
            return parent != null;
        }

        public boolean hasChild() {
            return child != null;
        }

        public int getCounter() {
            return counter;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public T getCurrentValue() {
            return validValues.get(counter);
        }
    }
}

