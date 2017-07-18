/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bloc97.infiniview.SpaceGame.Objects;

import com.bloc97.infiniview.SpaceGame.Objects.SpaceNatural.SpaceNaturalType;

/**
 *
 * @author bowen
 */
public abstract class SpaceRender {
    public static boolean canRenderNaturalNameByScale(SpaceNaturalType type, double scale) {
        switch (type) {
            case Massive:
                return true;
            case Big:
                if (scale > 7E-15) {
                    return true;
                }
            case Medium:
                if (scale > 3E-11) {
                    return true;
                }
                break;
            case Small:
                if (scale > 3E-10) {
                    return true;
                }
                break;
            case Tiny:
                if (scale > 1E-7) {
                    return true;
                }
                break;
        }
        return false;
    }
    public static boolean canRenderNaturalPathByScale(SpaceNaturalType type, double scale) {
        switch (type) {
            case Massive:
                return false;
            case Big:
                if (scale > 3E-11) {
                    return true;
                }
            case Medium:
                if (scale > 7E-12) {
                    return true;
                }
                break;
            case Small:
                if (scale > 6E-11) {
                    return true;
                }
                break;
            case Tiny:
                if (scale > 1E-7) {
                    return true;
                }
                break;
        }
        return false;
    }
    public static boolean canRenderNaturalObjectByScale(SpaceNaturalType type, double scale) {
        switch (type) {
            case Massive:
                return true;
            case Big:
                if (scale > 4E-19) {
                    return true;
                }
            case Medium:
                if (scale > 4E-12) {
                    return true;
                }
                break;
            case Small:
                if (scale > 4E-11) {
                    return true;
                }
                break;
            case Tiny:
                if (scale > 1E-8) {
                    return true;
                }
                break;
        }
        return false;
    }
}
