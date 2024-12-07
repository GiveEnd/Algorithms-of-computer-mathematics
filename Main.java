import java.util.ArrayList;
import java.util.List;

public class Main {
    // Класс для представления точки
    static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // Проверяет, находится ли точка внутри отсекателя
    private static boolean isInside(Point p, Point clip1, Point clip2) {
        return (clip2.x - clip1.x) * (p.y - clip1.y) - (clip2.y - clip1.y) * (p.x - clip1.x) >= 0;
    }

    // Находит точку пересечения между линией (p1, p2) и отсекателем (clip1, clip2)
    private static Point getIntersection(Point p1, Point p2, Point clip1, Point clip2) {
        double a1 = p2.y - p1.y;
        double b1 = p1.x - p2.x;
        double c1 = a1 * p1.x + b1 * p1.y;

        double a2 = clip2.y - clip1.y;
        double b2 = clip1.x - clip2.x;
        double c2 = a2 * clip1.x + b2 * clip1.y;

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return null; // Параллельные линии
        }

        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;

        return new Point(x, y);
    }

    // Алгоритм Сазерленда-Ходжмана
    public static List<Point> clipPolygon(List<Point> polygon, List<Point> clipper) {
        List<Point> outputList = new ArrayList<>(polygon);

        for (int i = 0; i < clipper.size(); i++) {
            Point clip1 = clipper.get(i);
            Point clip2 = clipper.get((i + 1) % clipper.size());

            List<Point> inputList = new ArrayList<>(outputList);
            outputList.clear();

            for (int j = 0; j < inputList.size(); j++) {
                Point current = inputList.get(j);
                Point prev = inputList.get((j - 1 + inputList.size()) % inputList.size());

                boolean currentInside = isInside(current, clip1, clip2);
                boolean prevInside = isInside(prev, clip1, clip2);

                if (currentInside) {
                    if (!prevInside) {
                        outputList.add(getIntersection(prev, current, clip1, clip2));
                    }
                    outputList.add(current);
                } else if (prevInside) {
                    outputList.add(getIntersection(prev, current, clip1, clip2));
                }
            }
        }

        return outputList;
    }

    public static void main(String[] args) {
        // Исходный полигон
        List<Point> polygon = new ArrayList<>();
        polygon.add(new Point(1, 1));
        polygon.add(new Point(5, 1));
        polygon.add(new Point(5, 5));
        polygon.add(new Point(1, 5));

        // Отсекатель (прямоугольник)
        List<Point> clipper = new ArrayList<>();
        clipper.add(new Point(2, 2));
        clipper.add(new Point(4, 2));
        clipper.add(new Point(4, 4));
        clipper.add(new Point(2, 4));

        List<Point> clippedPolygon = clipPolygon(polygon, clipper);

        System.out.println("Отсеченный полигон:");
        for (Point p : clippedPolygon) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }
}
