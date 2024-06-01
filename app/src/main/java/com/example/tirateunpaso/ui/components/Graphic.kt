package com.example.tirateunpaso.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.DpSize
import com.example.tirateunpaso.ui.values.defaultGraphicWidth
import com.example.tirateunpaso.ui.values.defaultGraphicHeight
import com.example.tirateunpaso.ui.values.defaultGraphicStrokeWidth

@Composable
fun Graphic(
    values: List<Offset>,
    yMinDefaultValue: Float? = null,
    graphicSize: DpSize = DpSize(
        defaultGraphicWidth,
        defaultGraphicHeight),
) {
    // Sample showing how to use the DrawScope receiver scope to issue
    // drawing commands
    Canvas(Modifier.size(graphicSize)) {
        drawRect(color = Color.White) // Draw grey background
        inset(40.0f, 40.0f) {
            // val quadrantSize = size / 2.0f
            val xMinValue = values.map { it.x }.min()
            val yMinValue = yMinDefaultValue ?:  values.map { it.y }.min()
            val xMaxValue = values.map { it.x }.max()
            val yMaxValue = values.map { it.y }.max()
            val xValueToSizeRelation = size.width / (xMaxValue - xMinValue)
            val yValueToSizeRelation = size.height / (yMaxValue - yMinValue)

            drawPoints(
                listOf(
                    Offset(0.0f, 0.0f),
                    Offset(0.0f, size.height),
                    Offset(size.width, size.height)),
                PointMode.Polygon,
                Color.Black,
                strokeWidth = defaultGraphicStrokeWidth
                )

            drawPoints(
                values.map {
                    Offset(
                        (it.x - xMinValue) * xValueToSizeRelation,
                        (yMinValue - it.y) * yValueToSizeRelation + size.height,
                    )},
                PointMode.Polygon,
                Color.Red,
                strokeWidth = defaultGraphicStrokeWidth)
        }
    }
}


@Preview
@Composable
fun DefaultStatisticsScreenPreview() {
    Graphic(
        listOf(
            Offset(1.0f, 100.0f),
            Offset(2.0f, 1200.0f),
            Offset(3.0f, 500.0f),
            Offset(5.0f, 800.0f),
            Offset(8.0f, 300.0f),
        ),
        yMinDefaultValue = 0.0f,
        DpSize(120.dp, 120.dp)
    )
}
