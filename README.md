## Rick and Morty App

In this application, data geting from an API containing the character information in the Rick and Morty cartoon series.
The application consists of two screens. These screens are Main screen and Detail Screen.

The first screen is the main screen. You can choose a character via view pager to see details of character.
Main screen contains these informations about characters:
- Id
- Name
- Image

![main_screen](https://user-images.githubusercontent.com/74800052/164953393-ec05a01c-0a83-4742-b51b-a96af470f72d.png = 250x250)

The second screen is the screen with the details of the characters. This screen contains more information about the characters.
Second screen open as Bottom Sheet Dialog.
Detail screen contains these informations about characters:
- Image
- Name
- Status
- Last known location (name/url)
- Species
- Gender
- Created

![detail_screen](https://user-images.githubusercontent.com/74800052/164953417-267ae319-a605-4826-8f9a-944424c87dff.png = 250x250)

API link : https://rickandmortyapi.com/

The application is developed using the MVVM structure in Kotlin.

Used for DI: Hilt - Dagger

Used for JSON: Moshi

Used for network: Retrofit
