package battleship.clientgui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class MapPanelDraw {
    private Graphics2D g2d;
    private MapTransform tr;
    private int grid_columns;
    private int grid_rows;
    private boolean fleet_board;
    
    public MapPanelDraw(Graphics2D g2d, MapTransform tr, int columns, int rows,
                        boolean fleet_board)
    {
        this.g2d = g2d;
        this.tr = tr;
        this.grid_columns = columns;
        this.grid_rows = rows;
        this.fleet_board = fleet_board;
    }

    public void color(int r, int g, int b)
    {
        g2d.setColor(new Color(r, g, b));
    }
    
    public void color(int r, int g, int b, int a)
    {
        g2d.setColor(new Color(r, g, b, a));
    }

    public void square(int x, int y, double inset)
    {
        double px = (x+inset) / grid_columns;
        double py = (y+inset) / grid_rows;
        double qx = (x+1-inset) / grid_columns;
        double qy = (y+1-inset) / grid_rows;
        
        MapTransform.Coord[] c = new MapTransform.Coord[4];
        c[0] = tr.transform(px, py);
        c[1] = tr.transform(qx, py);
        c[2] = tr.transform(qx, qy);
        c[3] = tr.transform(px, qy);
        
        Path2D.Double dp = new Path2D.Double();
        dp.moveTo(c[0].x, c[0].y);
        
        for (int i = 1; i < 4; i++) {
            dp.lineTo(c[i].x, c[i].y);
        }
        
        dp.closePath();
        
        g2d.fill(dp);
    }
    
    private Color darker(Color color, double p)
    {
        int r, g, b;
        float[] hsb = new float[3];
        
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        
        Color.RGBtoHSB(r, g, b, hsb);
        
        // lower brightness
        hsb[2] *= 1-p;
        
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    private void grid_outline()
    {
        double outset = 1.0/64;
        g2d.setColor(Color.black);
        PathTransform pt = new PathTransform(tr, 1, 1);
        pt.moveTo(-outset, -outset);
        pt.lineTo(1+outset, -outset);
        pt.lineTo(1+outset, 1+outset);
        pt.lineTo(-outset, 1+outset);
        pt.closePath();
        
        g2d.fill(pt.getPath());
    }
    
    public void grid()
    {
        PathTransform pt = new PathTransform(tr, 1, 1);
        
        Color bgcolor;
        
        if (fleet_board) {
            bgcolor = new Color(224, 224, 255);
        } else {
            bgcolor = new Color(255, 224, 192);
        }
        
        grid_outline();
        
        pt.moveTo(0, 0);
        pt.lineTo(1, 0);
        pt.lineTo(1, 1);
        pt.lineTo(0, 1);
        pt.closePath();
        MapTransform.Coord top, bottom;
        top = tr.transform(0, 0);
        bottom = tr.transform(0, 1);
        GradientPaint gp;
        gp = new GradientPaint(new Point2D.Double(0, top.y), bgcolor,
                               new Point2D.Double(0, bottom.y), darker(bgcolor, 0.1));
        g2d.setPaint(gp);
        g2d.fill(pt.getPath());
        
        g2d.setColor(new Color(0,0,0, 92));
        
        // draw column lines (vertical-ish)
        for (int i = 0; i < grid_columns + 1; i++) {
            MapTransform.Coord a, b;
            a = tr.transform((double)i / grid_columns, 0);
            b = tr.transform((double)i / grid_columns, 1);
            
            Line2D.Double l = new Line2D.Double(a.x, a.y, b.x, b.y);
            g2d.draw(l);
        }
        
        // draw row lines (horizontal)
        for (int i = 0; i < grid_rows + 1; i++) {
            MapTransform.Coord a, b;
            a = tr.transform(0, (double)i / grid_rows);
            b = tr.transform(1, (double)i / grid_rows);
            
            Line2D.Double l = new Line2D.Double(a.x, a.y, b.x, b.y);
            g2d.draw(l);
        }
    }
    
    public void ship(int x, int y, int length, boolean isHorizontal)
    {
        double inset = 0.25;
        
        PathTransform pt = new PathTransform(tr, grid_columns, grid_rows);
        pt.translate(x,  y);
        pt.moveTo(inset, inset);
        
        if (isHorizontal) {
            pt.lineTo(length-inset, inset);
            pt.lineTo(length-inset, 1-inset);
            pt.lineTo(inset, 1-inset);
        } else {
            pt.lineTo(1-inset, inset);
            pt.lineTo(1-inset, length-inset);
            pt.lineTo(inset, length-inset);
        }
        pt.closePath();
        
        g2d.fill(pt.getPath());
    }
}

class PathTransform {
    private MapTransform tr;
    private int columns;
    private int rows;
    private Path2D.Double path;
    double translate_x, translate_y;

    public PathTransform(MapTransform tr, int columns, int rows)
    {
        this.tr = tr;
        this.columns = columns;
        this.rows = rows;
        this.path = new Path2D.Double();
        this.translate_x = 0;
        this.translate_y = 0;
    }
    
    public void translate(double x, double y)
    {
        translate_x = x;
        translate_y = y;
    }
    
    public void moveTo(double x, double y)
    {
        MapTransform.Coord c;
        c = tr.transform((x + translate_x) / columns, (y + translate_y) / rows);
        path.moveTo(c.x, c.y);
    }
    
    public void lineTo(double x, double y)
    {
        MapTransform.Coord c;
        c = tr.transform((x + translate_x) / columns, (y + translate_y) / rows);
        path.lineTo(c.x, c.y);
    }
    
    public void closePath()
    {
        path.closePath();
    }
    
    public Path2D.Double getPath()
    {
        return path;
    }
}
