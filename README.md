# watch_video_java



Menu 
-

- **Menu**()
	- ***List<String[]>*** menu_list

----------

Menu\_list
-
- ***String*** url
- ***String*** text
- ***List<String[]>*** child\_list
	- return index(`id`) of text_list
- ***Child_list*** child(***int*** `id`)
	- return index(`id`) of child_list


----------
 
Child\_list
-
- ***String*** url
- ***String*** text


----------


Film 
-
- **Film**(***String*** `url`)
	- ***List<String[]>*** film_list
- ***void*** load\_new\_film()
	- update film_list to next page
- ***void*** goto_area(***String*** `area`)
	- update film_list to `area`
		- `area`:"香港","台灣","大陸","日本","韓國","歐美","泰國","新馬","印度","海外"
- ***void*** sort_by(***String*** `order`)
	- update film_list to `order`
		- `order`:"vod\_addtime","vod\_hits","vod\_hits\_month","vod\_hits\_day","vod\_hits\_week","vod\_gold","vod\_golder","vod\_up"
- ***Film_list*** film(int `id`)
	- return index(`id`) of film_list

----------

Film\_list
-
- ***String*** url
- ***String*** text
- ***String*** imsrc

----------

Episode 
-
- **Episode**(***String*** `url`)
	- ***List<String[]>*** episode\_list
- ***Child_list*** episode(***int*** `id`)
	- return index(`id`) of episode_list

----------

M3u8
-
- static ***String*** get\_m3u8(***String*** `url`)
	- return http://......index.m3u8
	- if not exit return "not exit"

----------

Search 
-
- **Search**(***String*** `keyword`)
	- **List<String[]>** film\_list
- ***Film_list*** film(int `id`)
	- return index(`id`) of film_list

----------



