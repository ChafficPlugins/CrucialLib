package io.github.chafficui.CrucialLib.Utils.customItems;

import io.github.chafficui.CrucialLib.Main;
import io.github.chafficui.CrucialLib.exceptions.CrucialException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CrucialHeadTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
        CrucialItem.CRUCIAL_ITEMS.clear();
    }

    @AfterEach
    void tearDown() {
        CrucialItem.CRUCIAL_ITEMS.clear();
        MockBukkit.unmock();
    }

    @Test
    void simpleConstructorSetsMaterialToPlayerHead() {
        CrucialHead head = new CrucialHead("trophy");
        assertEquals(String.valueOf(Material.PLAYER_HEAD), head.getMaterial());
        assertEquals("trophy", head.getType());
    }

    @Test
    void simpleConstructorHasNullOwner() {
        CrucialHead head = new CrucialHead("trophy");
        assertNull(head.getHeadOwner());
    }

    @Test
    void fullConstructorSetsHeadOwner() {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of("Rare"), recipe, "trophy", true, true, false);

        assertEquals(owner, head.getHeadOwner());
        assertEquals("Trophy", head.getName());
        assertEquals("PLAYER_HEAD", head.getMaterial());
    }

    @Test
    void registerWithoutOwnerThrowsException() {
        CrucialHead head = new CrucialHead("trophy");
        head.setName("Bad Head");
        head.setRecipe(new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"});

        CrucialException ex = assertThrows(CrucialException.class, head::register);
        assertTrue(ex.getMessage().contains("008"));
    }

    @Test
    void registerWithOwnerSucceeds() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of(), recipe, "trophy", true, true, false);
        head.register();

        assertTrue(head.isRegistered());
        assertTrue(CrucialItem.CRUCIAL_ITEMS.contains(head));
    }

    @Test
    void getItemStackWhenNotRegisteredReturnsNull() {
        CrucialHead head = new CrucialHead("trophy");
        assertNull(head.getItemStack());
    }

    @Test
    void getItemStackWhenRegisteredReturnPlayerHead() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of("A head"), recipe, "trophy", true, true, false);
        head.register();

        ItemStack stack = head.getItemStack();
        assertNotNull(stack);
        assertEquals(Material.PLAYER_HEAD, stack.getType());
    }

    @Test
    void getItemStackHasPersistentDataContainerId() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of(), recipe, "trophy", true, true, false);
        head.register();

        ItemStack stack = head.getItemStack();
        assertNotNull(stack.getItemMeta());
        UUID extractedId = CrucialItem.getId(stack);
        assertNotNull(extractedId);
        assertEquals(head.getId(), extractedId);
    }

    @Test
    void headInheritsFromCrucialItem() {
        CrucialHead head = new CrucialHead("trophy");
        assertInstanceOf(CrucialItem.class, head);
    }

    // --- Round-trip identification tests ---

    @Test
    void getByStackFindsRegisteredHead() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of(), recipe, "trophy", true, true, false);
        head.register();

        ItemStack stack = head.getItemStack();
        CrucialItem found = CrucialItem.getByStack(stack);
        assertNotNull(found, "getByStack should find the registered head");
        assertSame(head, found);
    }

    @Test
    void getIdRoundTripMatchesHeadId() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of(), recipe, "trophy", true, true, false);
        head.register();

        ItemStack stack = head.getItemStack();
        UUID extractedId = CrucialItem.getId(stack);
        assertEquals(head.getId(), extractedId, "Round-trip ID should match the head's UUID");
    }

    @Test
    void multipleGetItemStackCallsReturnConsistentId() throws CrucialException {
        UUID owner = UUID.randomUUID();
        String[] recipe = new String[]{"AIR", "AIR", "AIR", "AIR", "DIAMOND", "AIR", "AIR", "AIR", "AIR"};
        CrucialHead head = new CrucialHead("Trophy", owner, List.of(), recipe, "trophy", true, true, false);
        head.register();

        assertEquals(CrucialItem.getId(head.getItemStack()), CrucialItem.getId(head.getItemStack()));
    }
}
