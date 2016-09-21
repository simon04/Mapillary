// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.mapillary.mode;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.mapillary.MapillaryAbstractImage;
import org.openstreetmap.josm.plugins.mapillary.MapillaryData;
import org.openstreetmap.josm.plugins.mapillary.MapillaryLayer;

/**
 * Superclass for all the mode of the {@link MapillaryLayer}.
 *
 * @author nokutu
 * @see MapillaryLayer
 */
public abstract class AbstractMode extends MouseAdapter {
  protected static final int SNAP_DISTANCE = 10;

  protected MapillaryData data = MapillaryLayer.getInstance().getData();

  /**
   * Cursor that should become active when this mode is activated.
   */
  public int cursor = Cursor.DEFAULT_CURSOR;

  /**
   * Paint the dataset using the engine set.
   *
   * @param g {@link Graphics2D} used for painting
   * @param mv The object that can translate GeoPoints to screen coordinates.
   * @param box Area where painting is going to be performed
   */
  public abstract void paint(Graphics2D g, MapView mv, Bounds box);

}
