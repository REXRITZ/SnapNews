
# SnapNews

A short news android app that shows trending news with different category options and search functionality.

## App

### UI
The app consists of a single activity with multiple fragments using MVVM architecture-  

- Home Fragment  
  This contains a list of latest news with ability to filter by category using TabLayout and filter by country using Dialog
- Search Fragment  
  This view contains ability to search news using edittext
- News Detail Fragment  
  This view contains news in detail

### Libraries used

- **Android Jetpack**  
  RecyclerView, Navigation-UI, Material Design, Lifecycle components
- **Glide** (image rendering)
- **Network**  
  Retrofit, Gson(serialize / deserialize)

### App screenshots
<table>
  <td><img src="https://github.com/REXRITZ/SnapNews/blob/main/app%20ss/page1.png" width="220" /> </td>
  <td><img src="https://github.com/REXRITZ/SnapNews/blob/main/app%20ss/page2.png" width="220" /> </td>
  <td><img src="https://github.com/REXRITZ/SnapNews/blob/main/app%20ss/page3.png" width="220" /> </td>
</table>

### Api Info
The news data api used here is [newsdata.io](https://newsdata.io/docs)

#### Get latest news by category and country

```
  GET api/1/news
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `apikey` | `string` | **Required**. Your API key |
| `country` | `string` | country code |
| `category` | `string` | news category |
| `page` | `integer` | page number |

#### Search news

```
  GET api/1/news
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `apikey` | `string` | **Required**. Your API key |
| `q` | `string` | query to search for |
| `page` | `integer` | page number |

#### Add API Key
Add the following line in your project's root directory, inside the `local.properties` file (create one if unavailable) include the following line:
```
KEY="your api key"
```

## Connect with me
Hey! I am currently looking for a full-time role as a SDE / App Developer. If you like my project and have a work opportunity available, then do connect with me.    
email: rmbhadane33@gmail.com    
LinkedIn: https://www.linkedin.com/in/riteshbhadane/

