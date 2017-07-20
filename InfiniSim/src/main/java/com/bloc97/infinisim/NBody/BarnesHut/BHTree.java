/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infinisim.NBody.BarnesHut;
/**
 * BHTree.java
 *
 * Represents a quadtree for the Barnes-Hut algorithm.
 *
 * Dependencies: Body.java Quad.java
 *
 * @author bloc97, chindesaurus
 */

import com.bloc97.infinisim.Collision.Helpers;
import com.bloc97.infinisim.Spatial;
import com.bloc97.uvector.Vector;
import com.bloc97.uvector.Vectors;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BHTree {

    // threshold value
    private final double theta;
    private final double thetaSqr;

    private Spatial body;     // body or aggregate body stored in this node
    private Quad quad;     // square region that the tree represents
    private BHTree NW;     // tree representing northwest quadrant
    private BHTree NE;     // tree representing northeast quadrant
    private BHTree SW;     // tree representing southwest quadrant
    private BHTree SE;     // tree representing southeast quadrant
  
    /**
     * Constructor: creates a new Barnes-Hut tree with no bodies. 
     * Each BHTree represents a quadrant and an aggregate body 
     * that represents all bodies inside the quadrant.
     *
     * @param q the quadrant this node is contained within
     */
    public BHTree(Quad q, double tetha) {
        this.quad = q;
        this.body = null;
        this.NW = null;
        this.NE = null;
        this.SW = null;
        this.SE = null;
        this.theta = tetha;
        this.thetaSqr = tetha * tetha;
    }
 

    /**
     * Adds the Body b to the invoking Barnes-Hut tree.
     */
    public void insert(Spatial b) {
        
        // if body is outside of the quadrant, return
        if (!quad.contains(b.position().get(0), b.position().get(1))) {
            return;
        }

        // if this node does not contain a body, put the new body b here
        if (body == null) {
            body = Spatial.createCopy(b);
            return;
        }
  
        // internal node
        if (!isExternal()) {
            
            // update the center-of-mass and total mass
            updateBody(b);
            
            // recursively insert Body b into the appropriate quadrant
            putBody(b);
        }

        // external node
        else {
            // subdivide the region further by creating four children
            /*
            NW = new BHTree(quad.NW(), theta);
            NE = new BHTree(quad.NE(), theta);
            SE = new BHTree(quad.SE(), theta);
            SW = new BHTree(quad.SW(), theta);
            */
            // recursively insert both this body and Body b into the appropriate quadrant
            putBody(this.body);
            putBody(b);
            
            // update the center-of-mass and total mass
            updateBody(b);
        }
    }

    
    /**
     * Adds the Body b to the total center of mass and combined mass values of this branch of the tree.
     * @param b
     */
    private void updateBody(Spatial b) {
        Helpers.merge(body, b);
    }
    

    /**
     * Inserts a body into the appropriate quadrant.
     */ 
    private void putBody(Spatial b) {
        if (quad.NW().contains(b.position().get(0), b.position().get(1))) {
            if (NW == null) {
                NW = new BHTree(quad.NW(), theta);
            }
            NW.insert(b);
        } else if (quad.NE().contains(b.position().get(0), b.position().get(1))) {
            if (NE == null) {
                NE = new BHTree(quad.NE(), theta);
            }
            NE.insert(b);
        } else if (quad.SE().contains(b.position().get(0), b.position().get(1))) {
            if (SE == null) {
                SE = new BHTree(quad.SE(), theta);
            }
            SE.insert(b);
        } else if (quad.SW().contains(b.position().get(0), b.position().get(1))) {
            if (SW == null) {
                SW = new BHTree(quad.SW(), theta);
            }
            SW.insert(b);
        }
    }


    /**
     * Returns true iff this tree node is external.
     */
    private boolean isExternal() {
        // a node is external iff all four children are null
        return (NW == null && NE == null && SW == null && SE == null);
    }
    
    /**
     * Returns the approximate bodies to integrate against using
     * the Barnes-Hut tree.
     * @return Set of bodies to integrate against
     */
    public Set<Spatial> getBodies(Spatial b) {
        Set<Spatial> list = new LinkedHashSet<>();
        recurseGetBodies(b, list);
        return list;
    }
    
    /**
     * Returns the approximate bodies to integrate against using
     * the Barnes-Hut tree.
     * @return List of bodies to integrate against
     */
    public List<Spatial> getBodiesAsList(Spatial b) {
        List<Spatial> list = new LinkedList<>();
        recurseGetBodies(b, list);
        return list;
    }
    
    private void recurseGetBodies(Spatial b, Collection<Spatial> list) {
        
        if (body == null || b.equals(body)) {
            return;
        }
        
        if (isExternal()) {
            list.add(body);
        } else {
            
            // width of region represented by internal node
            double quadWidth = quad.length();
            double quadWidthSqr = quadWidth * quadWidth;
            
            // distance between Body b and this node's center-of-mass
            double distanceSqr = Vectors.sub(body.position(), b.position()).normSqr(); //Using the distance squared to bypass the need of square roots
            
            // compare ratio (s / d) to threshold value Theta
            if ((quadWidthSqr / distanceSqr) < thetaSqr) {
                list.add(body);
            } else { // recurse on each of current node's children
                if (NW != null) {
                    NW.recurseGetBodies(b, list);
                }
                if (NE != null) {
                    NE.recurseGetBodies(b, list);
                }
                if (SW != null) {
                    SW.recurseGetBodies(b, list);
                }
                if (SE != null) {
                    SE.recurseGetBodies(b, list);
                }
            }
        }
        
    }
    
    /**
     * Returns a string representation of the Barnes-Hut tree
     * in which spaces represent external nodes, and asterisks
     * represent internal nodes.
     *
     * @return a string representation of this quadtree
     */
    public String toString() {
        if (isExternal()) 
            return " " + body + "\n";
        else
            return "*" + body + "\n" + NW + NE + SW + SE;
    }
    
}