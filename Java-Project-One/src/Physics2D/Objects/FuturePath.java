/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics2D.Objects;

import Physics2D.Vector2;
import java.util.Date;

/**
 *
 * @author bowen
 */
public interface FuturePath {
    public void setOrbitPath(Vector2[] paths, Vector2[] vels, long[] timeStamps, Date date);
    public void setCurrentDate(Date date);
}
