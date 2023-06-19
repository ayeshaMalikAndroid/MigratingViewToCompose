/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.content.res.Configuration
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.widget.TextViewCompat
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable //Stateful: it is opinionated
//means easier to call
fun PlantDetailDescription(plantDetailViewModel: PlantDetailViewModel) {
    val currentPlant by plantDetailViewModel.plant.observeAsState()
    currentPlant?.let { plant ->
        PlantDetailDescription(plant)
    }
}

//this function doesn't contain any mutablestate so this is
//stateless function :Preview+reusable
@Composable
fun PlantDetailDescription(plant: Plant) {
    Surface {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.margin_normal))
                .fillMaxWidth()
        ) {
            PlantName(plant.name)
            // Watering ...
            PlantWatering(wateringInterval = plant.wateringInterval)
            // Plant Description...//
            PlantDescription(description = plant.description)
        }
    }
}

@Composable
fun PlantWatering(wateringInterval: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.margin_normal)
            )
    ) {
        Text(
            text = stringResource(id = R.string.watering_needs_prefix),
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.margin_small)),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold
        )

        val resources = LocalContext.current.resources
        val quantityString = resources.getQuantityString(
            R.plurals.watering_needs_suffix,
            wateringInterval,
            wateringInterval
        )
        Text(text = quantityString)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun PlantDetailDescPreview() {
    MdcTheme() {
        val fakePlant = Plant(
            "Avocado", "Apple",
            " My Html Code <h1> Html HEading</h1> <br/> <p> Paragraph </p>",
            1, 1, ""
        )
        PlantDetailDescription(plant = fakePlant)
    }
}

@Composable
fun PlantDescription(description: String) {
    //API to embed view types inside Compose is : AndroidView
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Medium)
            }
        },
        update = { tv ->
            tv.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)

        },
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.margin_small))
            .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
            .heightIn(min = dimensionResource(id = R.dimen.plant_description_min_height))

    )
}

@Composable
private fun PlantName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )
}


@Preview
@Composable
fun PreviewPlantName() {
    MdcTheme() {
        PlantName(name = "Avocado")
    }
}