package battleship.client;

import java.util.Iterator;

/**
 * Stores and manages a byte array of hit and missed squares on the target map
 *
 */
public class HitMissMap implements Iterable<Byte> {
    public static final byte UNTOUCHED=0;
    public static final byte HIT=1;
    public static final byte MISS=2;
    
    private int columns;
    private int rows;
    private byte[] hitMiss;
    
    public HitMissMap(int columns, int rows)
    {
        this.columns = columns;
        this.rows = rows;
        
        this.hitMiss = new byte[columns*rows];
    }
    
    public void setHitMiss(int x, int y, boolean hit)
    {
        this.hitMiss[y*columns + x] = hit?HIT:MISS;
    }

    @Override
    public Iterator<Byte> iterator()
    {
        return new Iterator<Byte>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < columns*rows;
            }

            @Override
            public Byte next() {
                byte val = hitMiss[index];
                index++;
                return val;
            }

            @Override
            public void remove() {
                // can't remove items from an array
                throw new UnsupportedOperationException();
            }
        };
    }
}
