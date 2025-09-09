package com.tfg.securerouter.ui.app.common.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close

@Composable
fun PasswordRequirements(
   password: String,
   modifier: Modifier = Modifier
) {
   val hasMinLen = password.length >= 12
   val hasUpper = password.any { it.isUpperCase() }
   val hasLower = password.any { it.isLowerCase() }
   val hasDigit = password.any { it.isDigit() }
   val hasSpecial = password.any { "!@#\$%^&*()-_=+[]{};:'\",.<>/?\\|`~".any(password::contains) }
   val noSpaces = !password.any { it.isWhitespace() }
   val notCommon = password.lowercase() !in setOf(
      "password","123456","12345678","qwerty","admin","letmein"
   )

   @Composable
   fun RowItem(ok: Boolean, text: String) {
      Row(
         modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
         horizontalArrangement = Arrangement.Start
      ) {
         Icon(
            imageVector = if (ok) Icons.Filled.Check else Icons.Filled.Close,
            contentDescription = null,
            tint = if (ok) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error
         )
         Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (ok) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant
         )
      }
   }

   Column(modifier = modifier) {
      Text(
         text = stringResource(R.string.password_requirements_title),
         style = MaterialTheme.typography.titleSmall
      )
      Spacer(Modifier.height(6.dp))
      RowItem(hasMinLen, stringResource(R.string.password_req_min_len, 12))
      RowItem(hasUpper,  stringResource(R.string.password_req_upper))
      RowItem(hasLower,  stringResource(R.string.password_req_lower))
      RowItem(hasDigit,  stringResource(R.string.password_req_digit))
      RowItem(hasSpecial,stringResource(R.string.password_req_special))
      RowItem(noSpaces,  stringResource(R.string.password_req_no_spaces))
      RowItem(notCommon, stringResource(R.string.password_req_not_common))
   }
}
