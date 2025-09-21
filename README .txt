Anime Finder App

Overview
	Anime Finder is an Android app that fetches and displays top anime from the Jikan API.
The app supports offline caching, pagination, search, and detailed anime view.

Features Implemented
	Fetch top anime list from Jikan API.
	Pagination support for browsing anime pages.
	Offline caching using Room database:
		Stores anime list per page.
		Allows browsing cached pages without internet.
	Search/filter anime list by:
		Title
		Genre
		Main cast
		Synopsis

Anime detail screen showing detailed info about selected anime.

Network monitoring:
	Detects internet availability.
	Automatically fetches data when network comes back online.

UI features:
	Loading indicators.
	Offline screen when no internet & no cached data.
	Smooth RecyclerView animations.
	Error handling for network/API failures.

Assumptions Made
	API response always contains data array of anime items.
	Each page of results has a consistent page number and can be cached independently.
	Internet connectivity is determined using ConnectivityManager and standard transports (WiFi, Cellular, Ethernet).
	Minimum supported SDK: 26, Target SDK: 36.

Known Limitations
No dynamic handling of lastVisiblePage from API — offline max page is derived from DB.
Pagination buttons (Next/Previous) rely on cached pages when offline; and shows blank pages if there is no data
Error messages are generic ("Unknown error") for failed API calls.
Images load via Glide; no placeholder for failed image loads yet.

Installation / Setup

Clone the repository:
	git clone <https://github.com/sahilbhaldar/AnimeFind.git>


Open in Android Studio.
	Build and run on an Android device (API 26+ recommended).
	Ensure internet is enabled for first fetch to populate DB.

Demonstration

Offline / Online Handling:

	Launch app offline → shows cached data if available, otherwise offline screen.

	Connect to internet → app automatically fetches missing data.

Pagination:

	Next/Previous buttons navigate through pages.

	Offline mode limits next page to max cached page.

Search:

	Type in search bar to filter current page results.

Anime Detail:

	Click an anime item → navigate to detail screen with full information.