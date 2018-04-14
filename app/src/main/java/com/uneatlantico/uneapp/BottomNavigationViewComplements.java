package com.uneatlantico.uneapp;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

/**
 * Complementa funciones de la barra de navegacion inferior.
 */

public class BottomNavigationViewComplements {

    /**
     * Evita que la BNV se eleve con el teclado
     * @param view
     */
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                item.setShiftingMode(false);

                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

        }
    }
}
