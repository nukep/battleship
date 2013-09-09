package battleship.gui;

class Coord {
    public double x, y;
}

/**
 * Responsible for transforming 3d plane coordinates (local) to and from
 * screen coordinates.
 * 
 * Local coordinates refer to a point on a unit square (1x1).
 * Screen coordinates refer to what's projected on the screen.
 */
class MapTransform {
    /* defines a trapezoid for our projected plane
     * left, right, y */
    private double top_l, bottom_l;
    private double top_r, bottom_r;
    private double top_y, height;
    /* right - left */
    private double top_w, bottom_w;
    
    public MapTransform(double top_l,    double top_r,
                        double bottom_l, double bottom_r,
                        double top_y,    double bottom_y)
    {
        this.top_l = top_l;
        this.bottom_l = bottom_l;
        
        this.top_r = top_r;
        this.bottom_r = bottom_r;
        this.top_w = top_r - top_l;
        this.bottom_w = bottom_r - bottom_l;
        
        this.top_y = top_y;
        this.height = bottom_y - top_y;
    }
    
    /**
     * Transform local (0..1) to screen
     */
    public Coord transform(double x, double y)
    {
        Coord c = new Coord();
        
        double a = getY(y);
        double l = lerp(top_l, bottom_l, a);
        double r = lerp(top_r, bottom_r, a);

        c.x = lerp(l, r, x);
        c.y = a * height + top_y;
        
        return c;
    }

    /**
     * Transform screen to local (0..1)
     */
    public Coord transformInverse(double x, double y)
    {
        Coord c = new Coord();
        
        c.y = getP(y);
        
        double a = getY(c.y);
        double l = lerp(top_l, bottom_l, a);
        double r = lerp(top_r, bottom_r, a);
        
        c.x = (x - l)/(r - l);
        
        return c;
    }
    
    /**
     * Linear intERPolation
     * @param a "beginning" value
     * @param b "ending" value
     * @param p progress (range is 0..1)
     * @return interpolated result
     */
    private double lerp(double a, double b, double p)
    {
        return (b-a)*p + a;
    }
    
    /**
     * @param p 0 = top, 1 = bottom
     * @return Width of projected plane intersection at "p"
     */
    private double getPWidth(double p)
    {
        return Math.pow(bottom_w / top_w, p) * top_w;
    }

    private double getY(double p)
    {
        return (getPWidth(p)-top_w)/(bottom_w - top_w);
    }
    
    private double getP(double y)
    {
        return Math.log((y - top_y)*(bottom_w - top_w)/height / top_w + 1)
                / Math.log(bottom_w/top_w);
    }
}