package com.example.beautymap.data.remote.model/*package com.example.beautymap.data.remote.model

import com.example.beautymap.domain.model.User


/* {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }
 */

data class RemoteUser (
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: RemoteAddress,
    val phone: String,
    val website: String,
    val company: RemoteCompany
)
data class RemoteAddress (
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: RemoteGeo
)
data class RemoteGeo (
    val lat: String,
    val lng: String
)
data class RemoteCompany (
    val name: String,
    val catchPhrase: String,
    val bs: String
)

*/