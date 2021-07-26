# Robomail-Revision

## Overview
You and your team of independent Software Contractors have been hired by Robotic Mailing Solutions Inc.
(RMS) to provide some much needed assistance in delivering the latest version of their product Automail
to market. Automail is an automated mail sorting and delivery system designed to operate in large buildings
that have dedicated mail rooms. It offers end to end receipt and delivery of mail items within the
building, and can be tweaked to fit many different installation environments. The system consists of two
key components:
1. Delivery Robots (see Figure 1) which take mail items from the mail room and deliver them throughout
the building. Each robot can hold one item in its “hands” and one item in its “tube” (a backpacklike
container attached to each robot). If a robot is holding two items (i.e., one in its hands and one
it its tube) it will always deliver the item in its hands first. An installation of Automail can manage
a team of delivery robots of any reasonable size (including zero!).
2. A Mail Pool subsystem which holds mail items after their arrival at the building’s mail room. The
mail pool decides the order in which mail items should be delivered.

The hardware of this system has beenwell tested, and the current software seems to perform reasonably
well but is not well documented. Currently, the robots offered by RMS are only able to move up or down
one floor in a building in one step. However, RMS recently developed a new capability for their robots
which allows them to go into an ‘overdrive’ mode. This ‘overdrive’ mode allows the robots to move twice
as fast (i.e., two floors up or down in one step) to deliver packages. However, due to the increased load
on the robotic hardware when using overdrive, there are two limitations placed on the robots when they
use the overdrive mode:
1. A robot using overdrive mode can only deliver one package. That is, it can carry a package in its
hands for delivery, but can not store an additional package in its tube.
2. Once a robot has delivered an item using overdrive, it needs to ‘cool down’ for 5 steps before it can
return to the mail room.

Because of these limitations RMS only wants to use the overdrive system to deliver packages of a very
high priority. Other packages should be delivered as normal.
Your task is to update the simulation software maintained by RMS to show how the overdrive ability
could be incorporated into the robot management system. In doing this you should apply your software
engineering and patterns knowledge to refactor and extend the system to support this new behaviour.
Note that the overdrive behaviour described in this document is just one possible use of this functionality.
When designing your solution you should consider that RMS may want to incorporate or trial other uses
of the overdrive ability in the future.
The behaviour of the system when delivering packages without a very high priority should not change.
Additionally, RMS expect the behaviour of the system to remain exactly the same when the overdrive
ability is turned off, or if no very high priority packages arrive.
RMS would also like you to add some statistics tracking to the software so that they can see how the
overdrive ability is being used. This will help them to understand the wear and tear that their robots are
going to go through. For now, RMS would like you to record:
1. The number of packages delivered normally (i.e., not using overdrive)
2. The number of packages delivered using overdrive
3. The total weight of the packages delivered normally (i.e., not using overdrive)
4. The total weight of the packages delivered using overdrive

When the simulation ends you should print this information to the console.
As with the overdrive behaviour, you should apply software engineering and patterns knowledge to support
this statistics tracking. Your design should consider that RMS may want to track additional statistical
information relating to robot performance in the future.
Once you have made your changes, your revised system will be benchmarked against the original
system to provide feedback on your results to RMS.
Other useful information
* The mailroom is on the ground floor (floor 0).
* All mail items are stamped with their time of arrival.
* Some mail items are priority mail items, and carry a priority from 10 (low) to 100 (high).
* Priority mail items arrive at the mailpool and are registered one at a time, so timestamps are unique
for priority items. Normal (non-priority) mail items arrive at the mailpool in batches, so all items
in a normal batch receive the same timestamp.
* The mailpool is responsible for giving mail items to the robots for delivery.
* A Delivery Robot carries at most two items, but it can be sent to deliver mail if it is only carrying
one item.
* All mail items have a weight. Delivery robots can carry items of any weight, and place items of any
weight in their tube.
* The system generates a measure of the effectiveness of the system in delivering all mail items, taking
into account time to deliver and priority. You do not need to be concerned about the detail of how
this measure is calculated.
* If a priority mail item has a priority of over 50, it is considered to have a very high priority. These
are the mail items that should be delivered using the overdrive ability (if the overdrive ability is
enabled).
* A robot using the overdrive ability will move two floors each step, unless it is only one floor away
from its destination. Then it will move just one floor each step. So, a robot using overdrive to deliver
a package from the mail room (which is on floor 0) to floor 9, it would go to floors 2, 4, 6, 8, then 9.