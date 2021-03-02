# cs0320 Term Project 2021

**Team Members:** Filip John Austin Raymond

**Team Strengths and Weaknesses:** 

Filip. 
-Pros:   
Good at seeing problems from different perspectives, thinking about repercussions of design decisions. 
-Cons:  
Bad at taking charge and focusing on incremental improvements. 
John  
-Pros:  
I’m somewhat decent at breaking down larger problems into more manageable and actionable smaller chunks   
-Cons:  
I’m pretty bad at important yet tedious processes like testwriting, and to some extent planning if it drags on too long.   
Also, still trying to figure out a good Github workflow with multiple people.   
Austin   
-Pros:  
Good at integrating and organizing code from multiple coders together, teamwork  
-Cons:  
Bad at planning out approaches to complex tasks/algorithms  
Raymond   
-Pros:   
Good at finding opportunities for abstraction and planning out class and package structure.  
-Cons:  
Bad at communication and meshing my ideas with multiple people  
Also not very good with time management and organization  


**Project Idea(s):** _Fill this in with three unique ideas!  
### Idea 1  
_Program Function Testing Assistant_  
Testing program functionality is pretty difficult and time consuming/boring. It’s very tedious to keep track of all the possible edge cases for a given function and also to write all of those tests as you go (or all at the very end…). We imagine making a piece of software that, given your code, would assist you in writing thorough tests for those functions! Functionalities might include:  
  _:Output-Based Suggestions:_ The program will collect a list of returns, executions, and errors thrown in your function including errors and executions thrown by helper functions called in your function, and automatically list out these outputs as something you should test for  
It will probably be most difficult to identify the many different paths that an input can take through a function/its helpers and how/why those paths might be taken, making it hard to identify the important outputs to consider in a complex function.  
  _:Input-Based Suggestions:_ The program will look at the input types and automatically generate test suggestions based on encoded common edge cases for that input type. For example: if one of the inputs is an integer it'll remind you to test for the case when the input is NEGATIVE, ZERO, or POSITIVE. If the type is something that hasn't been encoded yet then it'll remind you to please add those common edge cases to the file containing all of the encoded edge cases  
Again similar to above. If the input is not a standard data-type, it might be challenging to parse the code and identify what properties of the arguments would lead to unique edge cases.  
  _:Complex Function Test Reminder:_ Ideally when you are just forming the function, it'll create a small expandable popup at the side of the function where you can outline the more complex tests for the function, and which should be easily reachable/editable while you are writing the function in case you need to add more test or change some tests  
The hardest part of this would be learning how to integrate code into intellij. We also would need to keep track of these notes in some database somewhere to then reference later when writing tests.   
Also, to some extent this requires the development of a GUI that works with the IntelIJ Plugin, which could be more challenging on top of just learning how to setup a IntelIJ Plugin from scratch.   
  _:Is-Valid Checker:_ When you finish writing a function you can attach a property checker to the function that runs every time the function is called, which throws an error telling you exactly where this property checker was violated if it's violated anytime in the future (kind of like assert statements but constantly checking throughout runtime of the function). Might save some time with debugging but could potentially slow down runtime!
Might be difficult to attach something like this to the backend of running a java program (how would we access variables and stuff as it’s running? Not sure... yet…)  
  :This would ideally end up becoming an Intellij extension if we don't need to learn an entirely new language to do - otherwise could also be a dynamic text editor kind of thing.  
Obviously the hardest part of this would be learning and executing what an intellij plugin needs to work properly (hopefully in a language that we know!).
  

### Idea 2  
_Urban Exploration Yelp Platform_  
One idea is a centralized location to rate and review urban exploration locations. Features include:  
  _:Posting places you've explored_ including information about the views, difficulty/sketchiness, legality, etc.  
The difficulty will be maintaining this database and querying from it efficiently.  
  _:People could search locations_ using filters to find the kind of thing they’re interested in (also by proximity to them)  
Again, efficiency with querying the database and some kind of algorithm to find optimal match to search criteria  
  _:People could record the places they've explored_ (like a “trophy case” feature)  
  _:There could be a ranking system_ (some kind of elo type thing) for people who have visited many sites: higher rank for visiting a more difficult/rarer location.   
Need some sort of function for rating a location's “value” for this ranking system, maybe also based on other users who have/haven’t submitted.  
Need to make sure it doesn't promote excessive risk/legal issues [Honestly probably the biggest challenge]   
  _:People could post pictures, reviews, and log their adventures/interact with others_ (kind of Strava-esque).  
Again just a ton of data to keep track of - could be cool to have some sort of “routing” where it shows the path on a map to get to whatever site.  
  _:Maybe we could cycle through different challenges_ every month or so (like Strava), so maybe like visiting a certain amount of tunnels within a month or completing a certain areas (like visiting all the urban landmarks in that neighborhood)  


### Idea 3  
 _Course-At-Brown Extension_  
This idea was inspired by our own and other students' struggles with the current CAB website. We feel as if an extension program could make the current site more informative and convenient for users, and that this idea could have a large customer base.  
  _:Pathway Helper:_ This will probably be the most useful feature. Based on the concentration requirements from the Brown website, students will be able to select the concentrations there are interested in pursuing, and based on their previously taken courses the extension will be able to track which courses of these concentrations have been taken, which courses are the next ones to be taken from that concentration, and be able to offer options if permitting by the concentration.  
There is a lot of different data to track, both from outside sources and user input. One challenge could be saving the user's data even when they close the extension so they don't have to enter everything again.  
  _:Review Finder:_ The extension will be able to gather information from the Critical Review in order to give students a general idea of how each class being currently offered is.  
Similar challenges to the pathway helper feature, need to be able to efficiently and effectively get data from the Critical Review and convert it into what we need for the program.  
  _:Course Suggestion:_ The extension will be able to suggest both complements and substitutes. Based on the courses already added into the student's cart, the extension will be able to suggest similar courses to each course content-wise that could be potential substitutes in case the student doesn't enjoy one during the shopping period or one had no space. Based on the courses already added into the student's cart, the extension will be able to suggest courses that work well with each course when taken together. Both need to take into account what the student has already taken before and perform accordingly.  
The main challenge is figuring out how to program two versions of suggestions for each course based on the user's preference data. It's very difficult to make this suggestion programming perfect, but making this as accurate as possible could be very beneficial for users to use.  
  _:Cart Analyzer:_ Based on the courses already added into the student's cart and some other information they have entered earlier, the extension will be able to calculate an estimate of total hours per week the student will be spending in class and on classwork. Then based on the user's information the program will output whether this cart choice may be a good or bad idea for the student (too easy, too difficult, etc.).  
Calculating the total hours per week for each class should not be too difficult in theory, but being able to make accurate suggestions on whether the student's cart may or may not be a good idea based on their inputted data will definitely be challenging.  
The overall difficulty of this project is learning about website extensions and how to code a working one. Also, making the user interface friendly, appealing, and easy to use is an important challenge.  


**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
