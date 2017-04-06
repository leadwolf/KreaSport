KreaSport
=========


----------

## TODO ##

 - **MainActivity**
	 - Permissions
		 - custom frag/activity
		 - request + request receiver
	 - Transfer downloaded races to `ExploreFragment`
 - **HomeFragment**
	 - Check key format before download
 - **ExploreFragment**
	 - Everything:
		 - Map
		 - CustomIconOverlay
			 - Custom icons
		 - Save state
		 - Callbacks for interaction w/ `BottomSheetFragment`
		 - Bottom Sheet binding
			 - Need to initialize the binding in ExploreFragment instead of BottomSheetFragment and pass the value
 - **OfflineAreaActivity**
	 - Everything:
		 - Activity Layout
		 - Map
		 - Overlay
			 - Size estimation
		 - Aysnc download
			 - Notification w/ stop
		 - ArrayAdapter & Model
		 - Acitivity for downloaded areas
## DONE ##
 - **MainActivity**
	 - Navigation drawer
~~- Switch fragments~~
		 ~~- Save fragment state~~
		 ~~- Backstack~~
	 - Moved to activities
		 - Proper switching
		 - Force selected nav item to current activity
		 - Sub-activites use fragments, recreate each time bc the activity itself is destroyed when switching with the navigation drawer
	 - Callbacks
		 - Callback for downloads from `HomeFragment`
		 - Other callback methods in place
 - **HomeFragment**
	 - Layout
		 - Text & buttons
 - **ExploreFragment**
	 - CustomIconOverlay
		 - Proper adding w/o reloading, theory
	 - Bottom Sheet binding

----------


Project description
-------------------

The Mapsv3 app was started as a personal initiative to learn how to implement osmdroid in an app with multiple fragments, bottom sheets, custom overlays created from a list of races...

KreaSport aims to rebuild Mapsv3 with a new perspective. The goals for this app are the following:

 - Rebuild the app with the same features
 - Rebuild these features, this time hopefully being able to see the bigger picture and anticipate future expansion problems
 - Use clean code, separate classes properly, use callbacks, intents...
 - Implement a cleaner interface

Finally, once these goals are reached, we may be able to use this app as a clean start, replace Mapsv3 and be able to continue forwards according to the original goal of KreaSport which is to create an app for orienteering races.


----------
