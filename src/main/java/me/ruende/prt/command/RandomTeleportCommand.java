package me.ruende.prt.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTeleportCommand extends BukkitCommand {

    private final Random random = new Random();

    public RandomTeleportCommand() {
        super("rtp");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("이 커맨드는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        World world = player.getWorld();
        Location safeLocation = findRandomSafeLocation(world);

        if (isSafe(safeLocation)) {
            safeLocation.setYaw(player.getLocation().getYaw());
            safeLocation.setPitch(player.getLocation().getPitch());
            player.teleport(safeLocation);
            player.sendMessage("무작위 안전한 위치로 텔레포트되었습니다!");
        } else {
            player.sendMessage(String.format("이동하려는 위치 (%d, %d) 가 안전하지 않아, 이동에 실패했습니다. 다시 시도해 주세요.", safeLocation.getBlockX(), safeLocation.getBlockZ()));
        }
        return true;
    }

    private Location findRandomSafeLocation(World world) {
        int x = random.nextInt(20001) - 10000;
        int z = random.nextInt(20001) - 10000;
        List<Integer> safeYs = new ArrayList<>();

        for (int y = -64; y <= 320; y++) {
            Location loc = new Location(world, x + 0.5, y, z + 0.5);
            if (isSafe(loc)) {
                safeYs.add(y);
            }
        }

        if (!safeYs.isEmpty()) {
            int chosenY = safeYs.get(random.nextInt(safeYs.size()));
            return new Location(world, x + 0.5, chosenY, z + 0.5);
        }
        return new Location(world, x, 0, z);
    }

    private boolean isSafe(Location location) {
        location.getWorld();
        Location below = location.clone().subtract(0, 1, 0);
        Location head = location.clone().add(0, 1, 0);

        return isSolid(below) && isEmpty(location) && isEmpty(head);
    }

    private boolean isSolid(Location location) {
        Material mat = location.getBlock().getType();
        return mat.isSolid() && !(mat == Material.WATER || mat == Material.LAVA);
    }

    private boolean isEmpty(Location location) {
        Material mat = location.getBlock().getType();
        return mat == Material.AIR || mat == Material.CAVE_AIR || mat == Material.VOID_AIR;
    }
}