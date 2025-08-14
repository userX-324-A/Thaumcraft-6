package thaumcraft.common.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
// import net.minecraft.util.text.StringTextComponent; // no direct literals used anymore
import net.minecraft.util.text.TranslationTextComponent;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.client.golems.ClientSealCache;
import thaumcraft.common.menu.SealContainer;
import thaumcraft.common.network.NetworkHandler;
import thaumcraft.common.network.msg.RequestSealPropsChangeMessage;

/**
 * Minimal 1.16.5 GUI for editing seal properties: priority, color, locked, redstone, area (XYZ).
 */
public class SealScreen extends ContainerScreen<SealContainer> {
    private Button btnLocked;
    private Button btnRedstone;
    private Button btnColor;
    private java.util.List<Button> toggleButtons;
    private TextFieldWidget txtPriority;
    private TextFieldWidget txtAreaX;
    private TextFieldWidget txtAreaY;
    private TextFieldWidget txtAreaZ;

    public SealScreen(SealContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title == null ? new TranslationTextComponent("gui.thaumcraft.seal.title") : title);
        this.imageWidth = 176;
        this.imageHeight = 120;
    }

    @Override
    protected void init() {
        super.init();
        int x = this.leftPos + 10;
        int y = this.topPos + 18;

        ISealEntity ent = ClientSealCache.get(menu.getPos(), menu.getFace());
        boolean canEdit = canEdit(ent);
        byte priority = ent != null ? ent.getPriority() : 0;
        byte color = ent != null ? ent.getColor() : 0;
        boolean locked = ent != null && ent.isLocked();
        boolean redstone = ent != null && ent.isRedstoneSensitive();
        int ax = ent != null && ent.getArea() != null ? ent.getArea().getX() : 1;
        int ay = ent != null && ent.getArea() != null ? ent.getArea().getY() : 1;
        int az = ent != null && ent.getArea() != null ? ent.getArea().getZ() : 1;

        // Priority
        txtPriority = new TextFieldWidget(this.font, x + 80, y, 30, 18, new TranslationTextComponent("gui.thaumcraft.seal.priority_short"));
        txtPriority.setValue(String.valueOf(priority));
        txtPriority.setEditable(canEdit);
        txtPriority.setResponder(s -> {
            int v = clamp(getInt(s, 0), 0, 5);
            // Optimistic UI; send on each change
            sendProps(RequestSealPropsChangeMessage.Kind.PRIORITY, v, false, 0L);
        });
        this.addButton(txtPriority);

        // Color cycle
        btnColor = new Button(x + 120, y - 1, 60, 20, new net.minecraft.util.text.TranslationTextComponent("gui.thaumcraft.seal.color").append(":" + color),
                b -> {
                    if (!canEdit) return;
                    int next = (colorOf(btnColor.getMessage().getString()) + 1) & 15;
                    btnColor.setMessage(new net.minecraft.util.text.TranslationTextComponent("gui.thaumcraft.seal.color").append(":" + next));
                    sendProps(RequestSealPropsChangeMessage.Kind.COLOR, next, false, 0L);
                });
        btnColor.active = canEdit;
        this.addButton(btnColor);

        // Locked toggle
        btnLocked = new Button(x, y + 24, 80, 20, new TranslationTextComponent(locked ? "gui.thaumcraft.seal.locked_on" : "gui.thaumcraft.seal.locked_off"),
                b -> {
                    if (!canEdit) return;
                    boolean nv = !textOn(b.getMessage());
                    b.setMessage(new TranslationTextComponent(nv ? "gui.thaumcraft.seal.locked_on" : "gui.thaumcraft.seal.locked_off"));
                    sendProps(RequestSealPropsChangeMessage.Kind.LOCKED, 0, nv, 0L);
                });
        btnLocked.active = canEdit;
        this.addButton(btnLocked);

        // Redstone toggle
        btnRedstone = new Button(x + 90, y + 24, 80, 20, new TranslationTextComponent(redstone ? "gui.thaumcraft.seal.redstone_on" : "gui.thaumcraft.seal.redstone_off"),
                b -> {
                    if (!canEdit) return;
                    boolean nv = !textOn(b.getMessage());
                    b.setMessage(new TranslationTextComponent(nv ? "gui.thaumcraft.seal.redstone_on" : "gui.thaumcraft.seal.redstone_off"));
                    sendProps(RequestSealPropsChangeMessage.Kind.REDSTONE, 0, nv, 0L);
                });
        btnRedstone.active = canEdit;
        this.addButton(btnRedstone);

        // Area XYZ
        int ayBase = y + 52;
        txtAreaX = new TextFieldWidget(this.font, x + 18, ayBase, 24, 18, new TranslationTextComponent("gui.thaumcraft.seal.area_x"));
        txtAreaY = new TextFieldWidget(this.font, x + 18 + 30, ayBase, 24, 18, new TranslationTextComponent("gui.thaumcraft.seal.area_y"));
        txtAreaZ = new TextFieldWidget(this.font, x + 18 + 60, ayBase, 24, 18, new TranslationTextComponent("gui.thaumcraft.seal.area_z"));
        txtAreaX.setValue(String.valueOf(ax));
        txtAreaY.setValue(String.valueOf(ay));
        txtAreaZ.setValue(String.valueOf(az));
        txtAreaX.setEditable(canEdit);
        txtAreaY.setEditable(canEdit);
        txtAreaZ.setEditable(canEdit);
        this.addButton(txtAreaX);
        this.addButton(txtAreaY);
        this.addButton(txtAreaZ);

        this.addButton(new Button(x + 18 + 90, ayBase - 1, 60, 20, new TranslationTextComponent("gui.thaumcraft.seal.apply"), b -> {
            if (!canEdit) return;
            int vx = clamp(getInt(txtAreaX.getValue(), 1), 1, 32);
            int vy = clamp(getInt(txtAreaY.getValue(), 1), 1, 32);
            int vz = clamp(getInt(txtAreaZ.getValue(), 1), 1, 32);
            long packed = net.minecraft.util.math.BlockPos.asLong(vx, vy, vz);
            sendProps(RequestSealPropsChangeMessage.Kind.AREA, 0, false, packed);
        }));

        // Toggles (if any present in client cache)
        boolean[] toggles = thaumcraft.client.golems.ClientSealCache.getToggles(menu.getPos(), menu.getFace());
        toggleButtons = new java.util.ArrayList<>();
        if (toggles != null && toggles.length > 0) {
            int ty = ayBase + 26;
            for (int i = 0; i < toggles.length; i++) {
                final int idx = i;
                final boolean val = toggles[i];
                Button tb = new Button(x, ty + i * 22, 140, 20, new TranslationTextComponent(val ? "gui.thaumcraft.seal.toggle_on" : "gui.thaumcraft.seal.toggle_off", i), b -> {
                    boolean nv = !val;
                    b.setMessage(new TranslationTextComponent(nv ? "gui.thaumcraft.seal.toggle_on" : "gui.thaumcraft.seal.toggle_off", idx));
                    sendProps(RequestSealPropsChangeMessage.Kind.TOGGLE, idx, nv, 0L);
                });
                tb.active = canEdit;
                this.addButton(tb);
                toggleButtons.add(tb);
            }
        }

        if (!canEdit) {
            this.addButton(new Button(x, ayBase + 26, 160, 20, new TranslationTextComponent("gui.thaumcraft.seal.owner_only"), b -> {})).active = false;
        }
    }

    @Override
    protected void renderBg(@javax.annotation.Nonnull MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        // No background texture yet; draw a simple dark rect
        fill(ms, this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xAA000000);
    }

    private static int colorOf(String label) {
        int i = label.lastIndexOf(':');
        if (i >= 0) {
            try { return Integer.parseInt(label.substring(i + 1).trim()); } catch (Exception ignored) {}
        }
        return 0;
    }

    private static boolean textOn(ITextComponent msg) {
        String s = msg.getString();
        return s.endsWith("ON");
    }

    private boolean canEdit(ISealEntity ent) {
        if (ent == null) return false;
        if (this.minecraft == null) return false;
        net.minecraft.client.entity.player.ClientPlayerEntity p = this.minecraft != null ? this.minecraft.player : null;
        if (p == null) return false;
        if (p.abilities.instabuild) return true;
        String owner = ent.getOwner();
        return owner != null && owner.equals(p.getUUID().toString());
    }

    private static int getInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

    private void sendProps(RequestSealPropsChangeMessage.Kind kind, int intVal, boolean boolVal, long areaVal) {
        RequestSealPropsChangeMessage msg = new RequestSealPropsChangeMessage(menu.getPos(), menu.getFace(), kind, intVal, boolVal, areaVal);
        NetworkHandler.sendToServer(msg);
    }

    @Override
    public void render(@javax.annotation.Nonnull MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        drawLabels(ms);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    private void drawLabels(MatrixStack ms) {
        int x = this.leftPos + 10;
        int y = this.topPos + 8;
        this.font.draw(ms, new TranslationTextComponent("gui.thaumcraft.seal.title"), x, y, 0xFFFFFF);
        this.font.draw(ms, new net.minecraft.util.text.TranslationTextComponent("gui.thaumcraft.seal.priority"), x, y + 12, 0xA0A0A0);
        this.font.draw(ms, new net.minecraft.util.text.TranslationTextComponent("gui.thaumcraft.seal.area"), x, y + 46, 0xA0A0A0);
        this.font.draw(ms, new TranslationTextComponent("gui.thaumcraft.seal.area_x"), x + 6, y + 58, 0xA0A0A0);
        this.font.draw(ms, new TranslationTextComponent("gui.thaumcraft.seal.area_y"), x + 36, y + 58, 0xA0A0A0);
        this.font.draw(ms, new TranslationTextComponent("gui.thaumcraft.seal.area_z"), x + 66, y + 58, 0xA0A0A0);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Intercept priority numeric change commit on click-away
        if (txtPriority != null && !txtPriority.isFocused()) {
            int pr = clamp(getInt(txtPriority.getValue(), 0), 0, 5);
            sendProps(RequestSealPropsChangeMessage.Kind.PRIORITY, pr, false, 0L);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}



