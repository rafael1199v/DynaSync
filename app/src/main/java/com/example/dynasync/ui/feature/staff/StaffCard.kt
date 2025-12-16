package com.example.dynasync.ui.feature.staff

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dynasync.R
import com.example.dynasync.domain.model.Personal

@Composable
fun StaffCard(
    onDeleteStaff: () -> Unit,
    onUpdateStaff: () -> Unit,
    staff: Personal,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            12.dp
        ),
    )  {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(staff.imageUrl)
                .crossfade(true)
                .error(R.drawable.placeholder)
                .build(),
            placeholder = painterResource(id = R.drawable.placeholder),
            contentDescription = "Photo staff",
            modifier = Modifier.height(60.dp).width(60.dp).clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                8.dp
            ),
        ) {
            Text(
                text = "${staff.name} ${staff.lastname}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Cargo: ${staff.charge}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            )
        ) {
            IconButton(
                onClick = { onDeleteStaff() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_filled),
                    contentDescription = "Delete staff"
                )
            }

            IconButton(
                onClick = { onUpdateStaff() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_edit_square_24),
                    contentDescription = "Edit staff"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StaffCardPreview() {
    StaffCard(
        staff = Personal(
            id = 1,
            name = "Bill",
            lastname = "Gates",
            charge = "Developer",
            imageUrl = "https://imgs.search.brave.com/kLAi2FJoe1m0aSPk7FWSxVBUt4OWGGr0552zBv2_430/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/bW9zLmNtcy5mdXR1/cmVjZG4ubmV0L3NL/ZnJlTDV3YlgySmpR/U0pnRmpjdlMuanBn"
        ),
        onDeleteStaff = {},
        onUpdateStaff = {}
    )
}


