# cs0320 Term Project 2021

## Team Members:
 > CONTACT US @:
 > [Filip](mailto:filip_kierzenka@brown.edu) |
   [John](mailto:john_w_wu@brown.edu) |
   [Austin](mailto:austin_lang@brown.edu) |
   [Raymond](mailto:raymond_dai@brown.edu)

### Team Strengths and Weaknesses:

#### Filip
- **Pros:**   
  - Good at seeing problems from different perspectives, thinking about repercussions of design decisions.
- **Cons:**  
  - Bad at taking charge and focusing on incremental improvements. 

#### John  

- **Pros:**  
  - I’m somewhat decent at breaking down larger problems into more manageable and actionable smaller chunks   
- **Cons:**  
  - I’m pretty bad at important yet tedious processes like testwriting, and to some extent planning if it drags on too 
  long.   
  - Also, still trying to figure out a good Github workflow with multiple people.   

#### Austin  

- **Pros:**  
  - Good at integrating and organizing code from multiple coders together, teamwork  
- **Cons:**  
  - Bad at planning out approaches to complex tasks/algorithms  

#### Raymond   

- **Pros:**  
  - Good at finding opportunities for abstraction and planning out class and package structure.  
- **Cons:**  
  - Bad at communication and meshing my ideas with multiple people  
  - Also not very good with time management and organization  


## Project Idea(s): 
### Idea 1 - Program Function Testing Assistant  
Testing program functionality is pretty difficult and time-consuming/boring. It’s very tedious to keep track of all the 
possible edge cases for a given function and also to write all of those tests as you go (or all at the very end…). 
We imagine making a piece of software that, given your code, would assist you in writing thorough tests for those 
functions! <br> 

**Functionalities might include:**  
  - **Output-Based Suggestions:** <br> The program will collect a list of returns, executions, and errors thrown in your 
  function including errors and executions thrown by helper functions called in your function, and automatically list 
  out these outputs as something you should test for!
    -  It will probably be most difficult to identify the many different paths that an input can take through a function
    /its helpers and how/why those paths might be taken, making it hard to identify the important outputs to consider in 
    a complex function.
    

  - **Input-Based Suggestions:** <br>
  The program will look at the input types and automatically generate test suggestions based on encoded common edge 
  cases for that input type. For example: if one of the inputs is an integer it'll remind you to test for the case when 
  the input is NEGATIVE, ZERO, or POSITIVE. If the type is something that hasn't been encoded yet then it'll remind you 
  to please add those common edge cases to the file containing all the encoded edge cases
    - Again similar to above. If the input is not a standard data-type, it might be challenging to parse the code and 
    identify what properties of the arguments would lead to unique edge cases.
      

  - **Complex Function Test Reminder:** Ideally when you are just forming the function, it'll create a small expandable 
  popup at the side of the function where you can outline the more complex tests for the function, and which should be 
  easily reachable/editable while you are writing the function in case you need to add more test or change some tests  
    - The hardest part of this would be learning how to integrate code into intellij. We also would need to keep track 
    of these notes in some database somewhere to then reference later when writing tests.   
    - Also, to some extent this requires the development of a GUI that works with the IntelIJ Plugin, which could be 
    more challenging on top of just learning how to setup a IntelIJ Plugin from scratch.   
  
  - **Is-Valid Checker:** When you finish writing a function you can attach a property checker to the function that runs 
  every time the function is called, which throws an error telling you exactly where this property checker was violated 
  if it's violated anytime in the future (kind of like assert statements but constantly checking throughout runtime of 
  the function). Might save some time with debugging but could potentially slow down runtime!
    - Might be difficult to attach something like this to the backend of running a java program (how would we access 
    variables and stuff as it’s running? Not sure... yet…)
    
This would ideally end up becoming an Intellij extension if we don't need to learn an entirely new language to do - 
otherwise could also be a dynamic text editor kind of thing. The hardest part of this would be learning and executing 
what an intellij plugin needs to work properly (hopefully in a language that we know!).
  
TA Approval (dlichen): I don't think this is feasible for a variety of reasons, but if you are confident that it is, this is an acceptable level of complexity for a 32 project. I would go talk to Tim about this idea if you are serious about pursuing it and have concrete ideas of how to implement it.

### Idea 2 - Urban Exploration Yelp Platform 
One idea is a centralized location to rate and review urban exploration locations. 
**Functionalities might include:** 
  - **Posting places you've explored:** <br>
    Including information about the views, difficulty/sketchiness, legality, etc.  
    - The difficulty will be maintaining this database and querying from it efficiently.  
  
  - **People could search locations:** <br>
  Use filters to find the kind of thing they’re interested in (also by proximity to 
  them)
    - Again, efficiency with querying the database and some kind of algorithm to find optimal match to search criteria
    
- **People could record the places they've explored:** <br>
  (like a “trophy case” feature) <br>
  There could be a ranking system (some kind of elo type thing) for people who have visited many sites: higher rank for visiting a more difficult/rarer location.   
    - Need some sort of function for rating a location's “value” for this ranking system, maybe also based on other users who have/haven’t submitted.  
    - Need to make sure it doesn't promote excessive risk/legal issues [Honestly probably the biggest challenge]   
  
- **Strava-esque Features:** <br>
People could post pictures, reviews, and log their adventures/interact with others.  
  - Again just a ton of data to keep track of - could be cool to have some sort of “routing” where it shows the path on a map to get to whatever site.  
  - Maybe we could cycle through different challenges every month or so (like Strava), so maybe like visiting a certain amount of tunnels within a month or completing a certain areas (like visiting all the urban landmarks in that neighborhood)  

TA Approval (dlichen): I don't think this has any algorithmic complexity in it, so rejected for that reason but if there was some significant algorithmic focus you could put into this idea it could work. 

### Idea 3 - Course-At-Brown Extension  
This idea was inspired by our own and other students' struggles with the current CAB website. We feel as if an extension 
program could make the current site more informative and convenient for users, and that this idea could have a large 
customer base.  
  - **Pathway Helper:** <br>
  This will probably be the most useful feature. Based on the concentration requirements from the Brown website, students will be able to select the concentrations there are interested in pursuing, and based on their previously taken courses the extension will be able to track which courses of these concentrations have been taken, which courses are the next ones to be taken from that concentration, and be able to offer options if permitting by the concentration.  
    - There is a lot of different data to track, both from outside sources and user input. One challenge could be saving the user's data even when they close the extension so they don't have to enter everything again.  
  
- **Review Finder:** <br> 
  The extension will be able to gather information from the Critical Review in order to give students a general idea of how each class being currently offered is.  
    - Similar challenges to the pathway helper feature, need to be able to efficiently and effectively get data from the Critical Review and convert it into what we need for the program.  
  
- **Course Suggestion:** <br> 
The extension will be able to suggest both complements and substitutes. Based on the courses already added into the student's cart, the extension will be able to suggest similar courses to each course content-wise that could be potential substitutes in case the student doesn't enjoy one during the shopping period or one had no space. Based on the courses already added into the student's cart, the extension will be able to suggest courses that work well with each course when taken together. Both need to take into account what the student has already taken before and perform accordingly.  
    - The main challenge is figuring out how to program two versions of suggestions for each course based on the user's preference data. It's very difficult to make this suggestion programming perfect, but making this as accurate as possible could be very beneficial for users to use.  
  
- **Cart Analyzer:** <br> 
Based on the courses already added into the student's cart and some other information they have entered earlier, the extension will be able to calculate an estimate of total hours per week the student will be spending in class and on classwork. Then based on the user's information the program will output whether this cart choice may be a good or bad idea for the student (too easy, too difficult, etc.).  
    - Calculating the total hours per week for each class should not be too difficult in theory, but being able to make accurate suggestions on whether the student's cart may or may not be a good idea based on their inputted data will definitely be challenging.  

The overall difficulty of this project is learning about website extensions and how to code a working one. Also, making the user interface friendly, appealing, and easy to use is an important challenge.  

TA Approval (dlichen): I don't think there's an algorithmic complexity to this. You could definitely add an algorithmic focus to this idea though. Rejected until you come up with a significant central algorithm.

As none of your ideas have been approved, please resubmit either an edit to your existing ideas or a new idea by the end of the week! 

### New Ideas!:

### Idea - Artify
We want to make a program which will "artify" inputted images. The idea comes from on an artist's work seen here: https://external-preview.redd.it/P4rzcYo5BBXQHwAzobCSSPsaxQTbniPrBDOZid-dO6k.jpg?auto=webp&s=ed51a21ceb08653c12b6c6e2a47e516385208396
Features include:
- **Uploading your own photos and having the algorithm artify the image according to the selected style.:** <br>
- Difficulty would come from dealing with photos and efficient pixel manipulation of photos in Java, storing those photos somewhere in a DB.
- **Support for translating into various styles.:** <br>
-  Support for approximating the image with ellipses (seen in the linked photo), support for arbitrary shapes. Time permitting maybe an algorithm which will make an image look like a pencil drawing, into ASCII art, or a painting by having the "shapes" used in approximation look like brush strokes (experimentation needed).
-  This is the "algorithmic complexity" of this project. How can we optimize an algorithm which will make this kind of art effectively. The difficulty will come from dealing with realtively large photos and processing them quickly/effectively to make the desired output. Also, obviously, making sure that the algorithm is effective in making the art that we want!
- **Support for displaying your artified photos for others to view/like/comment, etc:** <br>
- Difficulty coming from hosting all of this information and creating a UI for people to use easily.

### Idea Edit - Courses at Brown
We could implement an algorithm here (some sort of path-finding algorithm) which would find the "easiest" path through a concentration at Brown. It would take information from the Critical Review into account (avg hours spent, overall difficulty, etc.) in order to construct the most balanced series of semesters to complete the requirements for a given concentration. You could input classes that you certainly want to take ("lock in" certain course for certain years) as well as other info specific to you ("I want a course with XYZ professor") - the algorithm will be constructing "nodes" in the graph of courses based on the metadata for those courses and then generating an optimal path through that graph based on the inputted constraints. This would be the algorithmic complexity behind this project.

TA Approval for resubmission: Both are approved. I'm concerned that the artify algorithm will become ML or DL rather than something you can implement but if you can come up with an algorithm that doesn't rely on external algorithms and is not trivial that sounds fine! Also for the CAB idea, it may be difficult to scrape cab (people have had issues in the past) so I would start seeing if that's possibel first but the algorithm sounds good!

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
