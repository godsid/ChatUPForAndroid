package com.uniteitsolution.chatup;


class User {
		
		
		private static String username, avatar, lat, lng, facebookId;
		static final String PREFS_ACCOUNT = "account";

		public void User(){
			//User.username = getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE)
			//getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().putString("username", user.getUsername()).commit();
		}
		
		public static String getUsername() {
			return username;
		}

		public static void setUsername(String username) {
			User.username = username;		
		}

		public static String getAvatar() {
			return avatar;
		}

		public static void setAvatar(String avatar) {
			User.avatar = avatar;
		}

		public static String getLat() {
			return lat;
		}

		public static void setLat(String lat) {
			User.lat = lat;
		}

		public static String getLng() {
			return lng;
		}

		public static void setLng(String lng) {
			User.lng = lng;
		}

		public static String getFacebookId() {
			return facebookId;
		}

		public static void setFacebookId(String facebookId) {
			User.facebookId = facebookId;
		} 
		
		
}