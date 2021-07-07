package com.example.marketplaceproyect.controller;

import com.example.marketplaceproyect.modelos.Products;

import java.util.Arrays;
import java.util.List;

/*
CODE CREDITS

MAX HEAP SORT https://tutorialspoint.dev/data-structure/heap-data-structure/heap-sort-for-decreasing-order-using-min-heap
MIN HEAP SORT https://algorithms.tutorialhorizon.com/heap-sort-java-implementation/
*/

public class heapSortPrecioProductos {

    public List<Products> heapSortMin(Products producto[]) {
        int size = producto.length;

        // Build heap
        for (int i = size / 2 - 1; i >= 0; i--)
            heapifyMin(producto, size, i);

        // One by one extract (Max) an element from heap and
        // replace it with the last element in the productoy
        for (int i = size - 1; i >= 0; i--) {

            //producto[0] is a root of the heap and is the max element in heap
            Products x = producto[0];
            producto[0] = producto[i];
            producto[i] = x;

            // call max heapify on the reduced heap
            heapifyMin(producto, i, 0);
        }
        return Arrays.asList(producto.clone());
    }

    public List<Products> heapSortMax(Products producto[]) {
        int n = producto.length;
        // Build heap (reproductoange productoay)
        for (int i = n / 2 - 1; i >= 0; i--)
            heapifyMax(producto, n, i);

        // One by one extract an element from heap
        for (int i = n - 1; i >= 0; i--) {

            // Move current root to end
            Products temp = producto[0];
            producto[0] = producto[i];
            producto[i] = temp;

            // call max heapify on the reduced heap
            heapifyMax(producto, i, 0);
        }
        return Arrays.asList(producto.clone());
    }


    // To heapify a subtree with node i
    void heapifyMin(Products producto[], int heapSize, int i) {
        int largest = i; // Initialize largest as root
        int leftChildIdx = 2 * i + 1; // left = 2*i + 1
        int rightChildIdx = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (leftChildIdx < heapSize && producto[leftChildIdx].getPrecio() > producto[largest].getPrecio())
            largest = leftChildIdx;

        // If right child is larger than largest so far
        if (rightChildIdx < heapSize && producto[rightChildIdx].getPrecio() > producto[largest].getPrecio())
            largest = rightChildIdx;

        // If largest is not root
        if (largest != i) {
            Products swap = producto[i];
            producto[i] = producto[largest];
            producto[largest] = swap;

            // Recursive call to  heapify the sub-tree
            heapifyMin(producto, heapSize, largest);
        }
    }


    // To heapify a subtree rooted with node i which is
    // an index in producto[]. n is size of heap
    void heapifyMax(Products producto[], int n, int i) {
        int smallest = i; // Initialize smalles as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is smaller than root
        if (l < n && producto[l].getPrecio() < producto[smallest].getPrecio())
            smallest = l;

        // If right child is smaller than smallest so far
        if (r < n && producto[r].getPrecio() < producto[smallest].getPrecio())
            smallest = r;

        // If smallest is not root
        if (smallest != i) {
            Products temp = producto[i];
            producto[i] = producto[smallest];
            producto[smallest] = temp;

            // Recursively heapify the affected sub-tree
            heapifyMax(producto, n, smallest);
        }
    }


}
