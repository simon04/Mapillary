// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.mapillary.mapmode;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.mapmode.MapMode;
import org.openstreetmap.josm.plugins.mapillary.MapillaryAbstractImage;
import org.openstreetmap.josm.plugins.mapillary.MapillaryLayer;
import org.openstreetmap.josm.tools.I18n;

/**
 *
 */
public class SelectMode extends MapMode {
  private static final int SNAP_DISTANCE = 10;

  /**
   * Creates a new map mode
   */
  public SelectMode() {
    super(I18n.tr("Select mode"), "mapillary-select", I18n.tr("Select, move and join images from Mapillary"), null, new Cursor(Cursor.DEFAULT_CURSOR));
  }


  @Override
  public void enterMode() {
    super.enterMode();
    Main.map.mapView.addMouseListener(this);
    Main.map.mapView.addMouseMotionListener(this);
  }

  @Override
  public void exitMode() {
    super.exitMode();
    Main.map.mapView.removeMouseListener(this);
    Main.map.mapView.removeMouseMotionListener(this);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
      MapillaryAbstractImage nearestImg = MapillaryLayer.getInstance().getClosestImage(e.getPoint(), SNAP_DISTANCE);
      MapillaryLayer.getInstance().getData().setSelectedImage(nearestImg);
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);
    if (Main.pref.getBoolean("mapillary.hover-enabled", true)) {
      MapillaryAbstractImage nearestImg = MapillaryLayer.getInstance().getClosestImage(e.getPoint(), SNAP_DISTANCE);
      MapillaryAbstractImage highlightedImg = MapillaryLayer.getInstance().getData().getHighlightedImage();

      if ((nearestImg == null && highlightedImg != null) || (nearestImg != null && !nearestImg.equals(highlightedImg))) {
        MapillaryLayer.getInstance().getData().setHighlightedImage(nearestImg);
        MapillaryLayer.getInstance().invalidate();
      }
    }
  }
}
