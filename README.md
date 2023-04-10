# teamR-HopDrop
Goal for Sprint 1:
We wanted to complete all the navigational elements, and things pertaining to the UI, while only using Firebase for the login and to populate the profile. We are putting everything in place so that for Sprint 2, we may use information from Firebase instead of pre-populated data.

What we have completed this sprint:
- Created a login page with Firebase authentication (jhed and password) with error prevention
  - Trying to log in with no inputs, a wrong username, or a wrong password that is not in Firebase (a login is provided at the bottom), will give you a corresponding message based on your error.
- Created the main activity with fragments for "Home", "Orders", and "Accounts".
- For "Home": 
  - We created the tab layout with the current orders and deliveries
  - It currently is NOT with real deliveries, it is pre-populated (ie. hardcoded), the implementation is detailed and planned in our sprint 2
  - Clicking between the tabs takes you to the different orders, and clicking on the details button will take you to the corresponding page, depending on if it is an order that you placed or one that you are agreeing to deliver.
  - For orders that you are DELIVERING:
    - It shows a page with the details, as well as an option to cancel the order or confirm that you have picked it up.
    - Clicking on "picked up" takes you to an updated page, where you can confirm that you have "delivered" the order. As of right now, this is just there to showcase that the buttons update the courier correctly, but we have not implemented it updating the user or notifying them.
    - Similarly, confirming the delivery takes you to the final page, "Confirm Delivery", which allows you to review the details one last time, before finalizing everything. Right now, it just takes you back to the home page, but in Sprint 2, it will prompt the courier to upload an image of the order delivered.
  - For orders that you have PLACED:
    - Clicking on the details button, takes you to a page with the details as well as a progress bar, which has not been implemented functionally, but the UI has been imported.
    - For sprint 2, as the courier clicks through each stage of the delivery process as described above, the progress bar will update in real time.
  - We added a FAB which is for New Orders, and it takes you to a separate activity page.
    - In here, you are prompted to enter the order details, such as "From", "To", "Fee", and "Additional Details". 
    - For error prevention, all fields are required except "Additional Details", and failing to fill out a required field prompts a Toast. 
    - Additionally, for each field, it limits the number of characters, and for the Fee, it only allows numerical inputs as well as decimals. Having an invalid input such as "4..3" with two decimals will prompt a corresponding toast error message.
    - For sprint 2, we will add a place to add a QR code that will be uploaded to Firebase, as well as change the "From" input to a dropdown as there is only a limited number of places to order from.
- For "Orders":
  - It shows a list of prepopulated orders, and in Sprint 2, we will get the orders from our firebase as described above. 
  - Inside the list, you can see the order details.
  - Clicking on the "details" button brings you to a page that lists everything including the "Additional Details", and has a button to confirm that this is an order that you would like to pick up.
  - Clicking on "Accept" takes you to the courier order details page, as described above. 
  - Backing out of this page takes you back to the home screen, where they can access the order details again, as described in the "Home" page.
- For "Profile":
  - Currently, the user does not have a picture, but we will be implementing that feature in Sprint 2.
  - It does go into Firebase and pull the User's first and last name, as well as check how many previous orders are stored in the Map attribute that we associated with a user.
  - Currently, the "PastOrders" can only be updated manually in Firebase, although in Sprint 2, this will be updated correctly.
  - The profile page shows two lists, one for the past deliveries the user has made, and one for the past orders they have placed. 
  - Clicking into the "details" button for either of the two lists will take you to an order details page which is empty as of right now, but for Sprint 2, we will pull the information for each order item in the Map described above.
  
For Testing:
Username: JohnDoe@jhu.edu (case insensitive)
Pass: HopDrop23 (case sensitive)

