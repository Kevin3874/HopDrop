# teamR-HopDrop
Goal for Sprint 2:
We wanted to complete all of the functionality for the app, including real-time order updating, order placement, order acceptance, and updating the order to the user's profile so they can view their past orders once it has been completed.

Note***: WE DO NOT HAVE AN ACCOUNT CREATION OPTION AS WE ARE MIMICING HOPKIN'S LOGIN AND IT DOES NOT ALLOW OUTSIDE USERS TO CREATE AN ACCOUNT WITH A JHU DOMAIN.
ADDITIONALLY, THERE IS NO OPTION TO CHANGE ANYTHING IN THE PROFILE SECTION, AS THIS SHOULD BE THEORETICALLY UNCHANGEABLE.

What we have completed this sprint:
- Fully implemented using Firebase.
- Orders placed are updated in real-time and appear in real-time on the Order's page.
- Order Accepted will dissapear from the Order's page so other user's will not be able to see it.
- Each Order placed is unique with its own QR-code image, details, associated order id, and other relevant information.
- The transaction takes place between the user and the courier in real-time, meaning that when the deliverer updates the order (example, saying it has been picked up), the update only happens beween those two, and does not affect any other people using the app.
- You can cancel the order, only before it has been picked up, and it will return to the orders page, and the user will see that the delivery has now been reverted back to waiting to being picked up.
- The Profile page is unique for each user, and will house their profile image, give them the information of how many orders they have delivered, and also show the list of all previous orders and deliveries.
  
For Testing:
Username: JohnDoe@jhu.edu (case insensitive)
Pass: HopDrop23 (case sensitive)

Username: JackSmith@jhu.edu (case insensitive)
Pass: HopDrop23 (case sensitive)

