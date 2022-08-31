


let arr = [1,2,3,4,5,6,7,8];
let start = 0;
let end = arr.length - 1;
let target = 8;

// Binary search recursive function
// The array must be sorted (ascending order)
function binarySearch(arr,start,end,target){
    //Find the mid element of the array
    mid = Math.floor((start + end )/ 2);
    //Check if the mid element is equal to the target
    //Base case for my recursive function 
    if (arr[mid] === target) return arr[mid] + " -> " + mid;
    
    // If the mid value is larger than the target check the first segment of the array and vice verser
    if(arr[mid] > target) {
        //Recursive call to check the first segment
        return binarySearch(arr,start,mid-1,target);
    } else{
        //Recursive call to check the second segment
        return binarySearch(arr,mid+1,end,target);
    }
}

// Calling the function
console.log(binarySearch(arr,start,end,target));