# Starting template

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project
A quiz application made for the course Object Orientated Programming Project (OOPP) at the TU Delft to reflect on daily energy usage.

## Group members

| Profile Picture                                                                                       | Name                  | Email                              |
|-------------------------------------------------------------------------------------------------------|-----------------------|------------------------------------|
| ![](https://eu.ui-avatars.com/api/?name=KS&length=4&size=50&color=DDD&background=777&font-size=0.325) | Konstantinos Stergiou | k.stergiou@student.tudelft.nl		    |
| ![](https://eu.ui-avatars.com/api/?name=DM&length=4&size=50&color=DDD&background=777&font-size=0.325) | Diana Micloiu         | D.Micloiu@student.tudelft.nl       |
| ![](https://eu.ui-avatars.com/api/?name=JK&length=4&size=50&color=DDD&background=777&font-size=0.325) | Jeroen Koelewijn      | J.G.Koelewijn@student.tudelft.nl   |
| ![](https://eu.ui-avatars.com/api/?name=AK&length=4&size=50&color=DDD&background=777&font-size=0.325) | Alan Kuźnicki 		      | a.l.kuznicki@student.tudelft.nl    | 
| ![](https://eu.ui-avatars.com/api/?name=CI&length=4&size=50&color=DDD&background=777&font-size=0.325) | Corina Ilie           | M.C.Ilie@student.tudelft.nl        |
| ![](https://eu.ui-avatars.com/api/?name=WB&length=4&size=50&color=DDD&background=777&font-size=0.325) | Wouter Breedveld		    | W.J.Breedveld-1@student.tudelft.nl |

## How to run it
Copy the file with all activities you want to load to `server/src/main/resources/activities.json` (name must also match). 
[Here 📎](https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/team-repositories/oopp-group-07/repository-template/uploads/82faf56bbfbe5fb81e7d7c26e27fba05/activities.json) is an example file.

Place all pictures belonging to the activities in their respective folder as specified by their `image_path` (as seen from `server/src/main/resources/`).
[Here 🖼️](https://we.tl/t-YGXkbQqiTF) is an example folder.

Sending a `DELETE` request to `HOST/api/activities` will filter all the currently loaded activities to be up to the same standard as those manually entered (removing wrong ones).

## How to contribute to it
Merge-request and issues may be made according to the template.

New activities can be added through the UI.

## Copyright / License (opt.)
