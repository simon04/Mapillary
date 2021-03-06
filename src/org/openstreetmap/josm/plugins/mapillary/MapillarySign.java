// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.mapillary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.mapillary.traffico.TrafficoSign;

/**
 * Each {@link MapillarySign} represents a traffic sign detected by the Mapillary's system.
 *
 * @author nokutu
 */
public class MapillarySign {

  private final static String[] COUNTRIES = {"au", "br", "ca", "eu", "us"};

  private static Map<String, HashMap<String, MapillarySign>> map;

  private final String fullName;
  private final String category;
  private final String type;
  private final String country;
  private String variant;
  private TrafficoSign trafficoSign;

  static {
    map = new HashMap<>();
    for (String country : COUNTRIES) {
      HashMap<String, MapillarySign> countryMap = new HashMap<>();
      try (
        BufferedReader br = new BufferedReader(new InputStreamReader(
          MapillarySign.class.getResourceAsStream("/data/signs/" + country + ".cson"), "UTF-8"
        ))
      ) {
        String line = "";
        while ((line = br.readLine()) != null) {
          if (!line.equals("")) {
            String[] pair = line.replace("'", "").split(":");
            countryMap.put(pair[0].trim(), new MapillarySign(pair[1].trim()));
          }
        }
        map.put(country, countryMap);

        br.close();
      } catch (IOException e) {
        Main.error(e);
      }
    }
  }

  public static MapillarySign getSign(String name, String country) {
    Map<String, MapillarySign> countryMap = map.get(country);
    if (countryMap == null) {
      throw new IllegalArgumentException("Country does not exist");
    }
    if (countryMap.containsKey(name)) {
      return countryMap.get(name);
    } else {
      if (name.split("--").length >= 3) {
        if (countryMap.containsValue(new MapillarySign(name))) {
          Optional<MapillarySign> p = countryMap.values().stream().filter(sign -> sign.toString().equals(name)).findFirst();
          assert p.isPresent();
          return p.get();
        } else {
          return new MapillarySign(name);
        }
      } else {
        Main.warn("Sign '" + name + "' does not exist in the plugin database. Please contact the developer to add it.");
        return null;
      }
    }
  }

  public MapillarySign(String fullName) {
    this.fullName = fullName;
    String[] parts = fullName.split("--");
    category = parts[0];
    type = parts[1];
    country = parts[2];
    if (parts.length == 4) {
      variant = parts[3];
    }
    trafficoSign = TrafficoSign.getSign(country, type);
  }

  public String getFullName() {
    return fullName;
  }

  public String getCategory() {
    return category;
  }

  public String getType() {
    return type;
  }

  public String getCountry() {
    return country;
  }

  public String getVariant() {
    return variant;
  }

  @Override
  public String toString() {
    return fullName;
  }

  @Override
  public int hashCode() {
    return fullName.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MapillarySign that = (MapillarySign) o;

    return fullName.equals(that.fullName);

  }
}
