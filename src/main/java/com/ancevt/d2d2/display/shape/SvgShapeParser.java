package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.SimpleContainer;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class SvgShapeParser {

    private float step = 8;

    public SvgShapeParser(float step) {
        this.step = step;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public Container parseAsset(String assetPath) {
        return parse(Assets.getAsset(assetPath));
    }

    public Container parse(InputStream inputStream) {
        try {
            return parseSvg(inputStream);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Container parseSvg(InputStream inputStream) throws Exception {
        Container result = new SimpleContainer();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        NodeList pathNodes = document.getElementsByTagName("path");

        for (int i = 0; i < pathNodes.getLength(); i++) {
            Element pathElement = (Element) pathNodes.item(i);
            String dAttribute = pathElement.getAttribute("d");
            String styleAttribute = pathElement.getAttribute("style");

            Color color = getFillColorFromStyleAttribute(styleAttribute);

            drawPathData(dAttribute, color, result);
        }

        return result;
    }

    private static Color getFillColorFromStyleAttribute(String styleAttribute) {
        Map<String, String> m = parseStyle(styleAttribute);
        String fill = m.get("fill");
        fill = fill.substring(1);
        return Color.of(fill);
    }

    private static Map<String, String> parseStyle(String style) {
        Map<String, String> styleMap = new HashMap<>();
        String[] attributes = style.split(";");

        for (String attribute : attributes) {
            String[] keyValue = attribute.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                styleMap.put(key, value);
            }
        }

        return styleMap;
    }

    /*

    m 28.117531,58.143127
    c 92.222689,0.94158 67.929884,48.020613 67.929884,48.020613
    L 153.40543,78.387108 157.2293,135.35273 88.849546,184.31492 44.312734,160.30461 19.570062,123.34758
    c 0,0 25.86734,-19.06701 26.767074,-20.24398
    C 47.23687,101.92662 33.290999,86.625938 33.290999,86.625938
    L 12.82206,76.974739
    Z

     */


    private void drawPathData(String d, Color color, Container container) {
        List<Command> commands = parsePathData(d);

        FreeShape shape = null;

        for (Command command : commands) {

            switch (command.command) {
                case "M", "m" -> {
                    if (shape != null) {
                        shape.compile();
                        container.addChild(shape);
                    }
                    shape = new FreeShape();
                    shape.setColor(color);
                    linesTo(shape, command.coordsList, command.command.equals("m"));
                }
                case "L", "l" -> linesTo(shape, command.coordsList, command.command.equals("l"));
                case "H", "h" -> horLineTo(shape, command.coordsList, command.command.equals("h"));
                case "V", "v" -> vertLineTo(shape, command.coordsList, command.command.equals("v"));
                case "C", "c" -> {
                    curveTo(shape, command.coordsList, command.command.equals("c"));
                }
                case "Z", "z" -> linesToBegin(shape);
            }


        }

        shape.compile();
        container.addChild(shape);
    }

    private void curveTo(FreeShape shape, List<Coords> coordsList, boolean relative) {
        shape.curveTo(
            coordsList.get(0).x + (relative ? shape.getCurrentX() : 0f),
            coordsList.get(0).y + (relative ? shape.getCurrentY() : 0f),

            coordsList.get(1).x + (relative ? shape.getCurrentX() : 0f),
            coordsList.get(1).y + (relative ? shape.getCurrentY() : 0f),

            coordsList.get(2).x + (relative ? shape.getCurrentX() : 0f),
            coordsList.get(2).y + (relative ? shape.getCurrentY() : 0f),

            step


        );
    }

    private static void vertLineTo(FreeShape shape, List<Coords> coordsList, boolean relative) {
        for (Coords c : coordsList) {
            shape.vertex(shape.getCurrentX(), c.value + (relative ? shape.getCurrentY() : 0f));
        }
    }

    private static void horLineTo(FreeShape shape, List<Coords> coordsList, boolean relative) {
        for (Coords c : coordsList) {
            shape.vertex(c.value + (relative ? shape.getCurrentX() : 0f), shape.getCurrentY());
        }
    }

    private void linesToBegin(FreeShape shape) {
        shape.closePath();
    }

    private static void linesTo(FreeShape shape, List<Coords> coordsList, boolean relative) {
        for (Coords c : coordsList) {
            shape.vertex(c.x + (relative ? shape.getCurrentX() : 0f), c.y + (relative ? shape.getCurrentY() : 0f));
        }

    }

    private static List<Command> parsePathData(String path) {
        StringTokenizer st = new StringTokenizer(path, " ");

        List<Command> result = new ArrayList<>();

        Command cur = null;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (Character.isLetter(token.charAt(0))) {
                if (cur != null) {
                    result.add(cur);
                }
                cur = new Command(token);
            } else {
                Coords coords = new Coords(token);
                cur.coordsList.add(coords);
            }


        }
        return result;
    }


//    public static void main(String[] args) {
//        String s = "M 20.694729,23.540043 41.163668,152.06578 169.15077,77.680925 Z";
//        var a = parsePathData(s);
//        System.out.println(a);
//    }


    @RequiredArgsConstructor
    @ToString
    private static class Command {
        private final String command;
        private final List<Coords> coordsList = new ArrayList<>();
    }

    @ToString
    private static class Coords {
        private float x;
        private float y;
        private float value;

        public Coords(String string) {
            if (string.contains(",")) {
                x = Float.parseFloat(string.split(",")[0]);
                y = Float.parseFloat(string.split(",")[1]);
            } else {
                value = Float.parseFloat(string);
            }
        }
    }

}
