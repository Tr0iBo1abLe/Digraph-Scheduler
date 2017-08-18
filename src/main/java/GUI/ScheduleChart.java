package GUI;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ScheduleChart<X, Y> extends XYChart<X, Y> {

    @Getter
    @Setter
    private double blockHeight = 10;

    public ScheduleChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.observableArrayList());
    }

    public ScheduleChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<Series<X, Y>> data) {
        super(xAxis, yAxis);
        if (!(xAxis instanceof ValueAxis && yAxis instanceof CategoryAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, X should be NumberAxis and Y should be a Category Axis");
        }
        setData(data);
    }

    private static String getStyleClass(Object obj) {
        return ((ExtraData) obj).getStyleClass();
    }

    private static double getLength(Object obj) {
        return ((ExtraData) obj).getLength();
    }

    private static String getLabel(Object obj) {
        return ((ExtraData) obj).getLabel();
    }

    @Override
    protected void layoutPlotChildren() {
        getData().forEach(e -> getDisplayedDataIterator(e).forEachRemaining(n -> {
            double x, y;
            x = getXAxis().getDisplayPosition(n.getXValue());
            y = getYAxis().getDisplayPosition(n.getYValue());
            if (Double.isNaN(x) || Double.isNaN(y)) return;
            Node block = n.getNode();
            Rectangle rect;
            @NonNull final String label = getLabel(n.getExtraValue());
            Text text = new Text(label);
            text.setTranslateX(x);
            text.setTranslateY(getBlockHeight());
            text.setBoundsType(TextBoundsType.VISUAL);
            if (block != null) {
                if (block instanceof StackPane) {
                    StackPane region = (StackPane) block;
                    if (region.getShape() == null) {
                        rect = new Rectangle(getLength(n.getExtraValue()), getBlockHeight());
                    } else if (region.getShape() instanceof Rectangle) {
                        rect = (Rectangle) region.getShape();
                    } else {
                        return;
                    }
                    y -= getBlockHeight() / 2.0f;
                    text.setTranslateX(getLength(n.getExtraValue()) / 1.0f);
                    text.setTranslateY(getBlockHeight() / 2.0f);
                    if (!region.getChildren().contains(rect) && !region.getChildren().contains(text)) {
                        region.getChildren().addAll(text);
                    }
                    rect.setWidth(getLength(n.getExtraValue()) * ((NumberAxis) getXAxis()).getScale());
                    rect.setHeight(getBlockHeight());
                    region.setShape(null);
                    region.setShape(rect);
                    region.setScaleShape(false);
                    region.setCenterShape(false);
                    region.setCacheShape(false);
                    block.setLayoutX(x);
                    block.setLayoutY(y);
                }
            }
        }));

    }

    @Override
    protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {
        Node block = createContainer(series, getData().indexOf(series), item, itemIndex);
        getPlotChildren().add(block);
    }

    @Override
    protected void dataItemRemoved(final Data<X, Y> item, final Series<X, Y> series) {
        final Node block = item.getNode();
        getPlotChildren().remove(block);
        removeDataItemFromDisplay(series, item);
    }

    @Override
    protected void dataItemChanged(Data<X, Y> item) {
    }

    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {
        for (int j = 0; j < series.getData().size(); j++) {
            Data<X, Y> item = series.getData().get(j);
            Node container = createContainer(series, seriesIndex, item, j);
            getPlotChildren().add(container);
        }
    }

    @Override
    protected void seriesRemoved(final Series<X, Y> series) {
        for (XYChart.Data<X, Y> d : series.getData()) {
            final Node container = d.getNode();
            getPlotChildren().remove(container);
        }
        removeSeriesFromDisplay(series);
    }

    private Node createContainer(Series<X, Y> series, int seriesIndex, final Data<X, Y> item, int itemIndex) {
        Node container = item.getNode();

        if (container == null) {
            container = new StackPane();
            item.setNode(container);
        }

        container.getStyleClass().add(getStyleClass(item.getExtraValue()));

        return container;
    }

    @Override
    protected void updateAxisRange() {
        final Axis<X> xa = getXAxis();
        final Axis<Y> ya = getYAxis();
        List<X> xData = xa.isAutoRanging() ? new ArrayList<>() : null;
        List<Y> yData = ya.isAutoRanging() ? new ArrayList<>() : null;
        if (xData != null || yData != null) {
            getData().forEach(a -> a.getData().forEach(b -> {
                if (xData != null) {
                    xData.add(b.getXValue());
                    xData.add(xa.toRealValue(xa.toNumericValue(b.getXValue()) + getLength(b.getExtraValue())));
                }
                if (yData != null) {
                    yData.add(b.getYValue());
                }
            }));
            if (xData != null) xa.invalidateRange(xData);
            if (yData != null) ya.invalidateRange(yData);
        }
    }

    public static class ExtraData {
        @Getter
        @Setter
        protected long length;
        @Getter
        @Setter
        protected String styleClass;
        @Getter
        @Setter
        protected String label;

        public ExtraData(long lengthMs, String styleClass, String label) {
            this.length = lengthMs;
            this.styleClass = styleClass;
            this.label = label;
        }
    }
}
