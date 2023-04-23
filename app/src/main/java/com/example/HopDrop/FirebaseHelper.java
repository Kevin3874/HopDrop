package com.example.HopDrop;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnOrderStateChangedListener {
        void onOrderStateChanged(Order order);

        void onOrderRemoved(Order order);

        void onError(Exception error);
    }

    public void addOrderToOrders(Order order) {
        db.collection("orders").document(order.getOrderID()).set(order);
    }

    public void addOrderToCurrentOrders(String userEmail, Order order) {
        db.collection("user_id").document(userEmail).update("currentOrders", FieldValue.arrayUnion(order));
    }

    public void addOrderToCurrentDeliveries(String delivererEmail, Order order) {
        db.collection("user_id").document(delivererEmail).update("currentDeliveries", FieldValue.arrayUnion(order));
    }

    public void removeOrderFromOrders(String orderId) {
        db.collection("orders").document(orderId).delete();
    }

    public void removeOrderFromCurrentOrders(String userEmail, Order order) {
        db.collection("user_id").document(userEmail).update("currentOrders", FieldValue.arrayRemove(order));
    }

    public void removeOrderFromCurrentDeliveries(String delivererEmail, Order order) {
        db.collection("user_id").document(delivererEmail).update("currentDeliveries", FieldValue.arrayRemove(order));
    }

    public ListenerRegistration setupOrderStateListener(String delivererEmail, String orderId, final OnOrderStateChangedListener listener) {
        return db.collection("user_id").document(delivererEmail)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        listener.onError(error);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        List<Order> previousOrders = snapshot.get("currentDeliveries", new ArrayList<Order>().getClass());
                        List<Order> currentOrders = new ArrayList<>(previousOrders);

                        List<Order> removedOrders = new ArrayList<>(previousOrders);
                        removedOrders.removeAll(currentOrders);
                        removedOrders.removeIf(order -> !order.getOrderID().equals(orderId));

                        for (Order removedOrder : removedOrders) {
                            listener.onOrderRemoved(removedOrder);
                        }

                        List<Order> addedOrModifiedOrders = new ArrayList<>(currentOrders);
                        addedOrModifiedOrders.removeAll(previousOrders);
                        addedOrModifiedOrders.removeIf(order -> !order.getOrderID().equals(orderId));

                        for (Order addedOrModifiedOrder : addedOrModifiedOrders) {
                            listener.onOrderStateChanged(addedOrModifiedOrder);
                        }
                    }
                });
    }



    public void removeOrderStateListener(ListenerRegistration listenerRegistration) {
        listenerRegistration.remove();
    }
}
