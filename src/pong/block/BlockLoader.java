package pong.block;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockLoader {
    private static boolean init=false;
    private final static ArrayList<ArrayList<Block>> data=new ArrayList<>();

    public static ArrayList<Block> getLevel(int level) {
        final int levelIndex=level-1;

        return data.get(level);
    }
}
