package com.example.triyngbuttons
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.triyngbuttons.ui.theme.TriyngButtonsTheme



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext





class MainActivity : ComponentActivity() {
    private lateinit var db: StationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            StationDatabase::class.java, "station-database"
        ).build()

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            // Pre-populate the database with some stations
            db.stationDao().insert(StationEntity(name = "Central Park", distance = 5))
            db.stationDao().insert(StationEntity(name = "Times Square", distance = 10))
            db.stationDao().insert(StationEntity(name = "Grand Central", distance = 15))
            // Add more stations as needed

//             Retrieve the stations from the database
            val stationEntities = db.stationDao().getAllStation()
            val stations_x: List<Station> = stationEntities.map { entity ->
                Station(name = entity.name, distance = entity.distance)
            }
            withContext(Dispatchers.Main) {
                setContent {
                    var currentStationIndex by remember { mutableStateOf(0) }
                    var useMiles by remember { mutableStateOf(false) }

                    JourneyApp(
                        stations = stations_x,
                        currentStationIndex = currentStationIndex,
                        useMiles = useMiles,
                        onUnitToggle = { useMiles = !useMiles },
                        onNext = {
                            if (currentStationIndex < stationEntities.size - 1) {
                                currentStationIndex++
                            }
                        },
                        onPrev = {
                            if (currentStationIndex > 0) {
                                currentStationIndex--
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun MyCustomTextView(customText: String) {
    Text(text = customText)
}

@Composable
fun MyApp(customText: String) {
    MyCustomTextView(customText = customText)
}

@Composable
fun StationInfoinKMS(station: Station) {
    Text(text = "${station.name}: ${station.distance} km")
}
@Composable
fun StationInfoinMiles(station: Station) {
    Text(text = "${station.name}: ${station.distance/1.609} miles")
}


@Composable
fun JourneyApp(stations: List<Station>, currentStationIndex: Int, onUnitToggle: () -> Unit, onNext: () -> Unit, onPrev: () -> Unit, useMiles: Boolean) {
    var totalDistanceCovered = 0
    for (i in 0 until currentStationIndex + 1) {
        totalDistanceCovered += stations[i].distance
    }
    val totalDistance = stations.sumOf { it.distance }
    val distanceToFinalStop = totalDistance - totalDistanceCovered
    val progress = totalDistanceCovered.toFloat() / totalDistance

    Column {
        Text(text = "Current Station: ${stations[currentStationIndex].name}")
        Text(text = "Total Distance Covered: ${if (useMiles) totalDistanceCovered / 1.609 else totalDistanceCovered} ${if (useMiles) "miles" else "km"}")
        Text(text = "Distance to Final Stop: ${if (useMiles) distanceToFinalStop / 1.609 else distanceToFinalStop} ${if (useMiles) "miles" else "km"}")
        LinearProgressIndicator(progress = progress)
        Text(text = "Next Stations:")
        Box(modifier = Modifier.height(200.dp)) { // Set a fixed height for the scrollable area
            LazyColumn {
                items(stations.subList(currentStationIndex + 1, stations.size)) { station ->
                    if (useMiles) {
                        StationInfoinMiles(station = station)
                    } else {
                        StationInfoinKMS(station = station)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = onPrev) {
                Text(text = "Prev")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onUnitToggle) {
                Text(text = if (useMiles) "Switch to KM" else "Switch to Miles")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onNext) {
                Text(text = "Next")
            }
        }
    }
}