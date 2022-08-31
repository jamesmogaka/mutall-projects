//a statement
// statements should be terminated by a ;
//comment are indicated by (//)
// console.log is used to print out to the console (stdout)
console.log("Hello World");

// variables are declared by let keyword
//By default variables are undefined
//var was the keyword that was used initially to declare variables(has flaws)
let names = "james";
console.log(names);

//constants are variables which are immutable once assigned
//They are declared by the const keyword
const pi = 3.142;
console.log(pi);
// pi = 3.143; Reassignment of constants bring about an error

//Types
//js primitive types(numbers,strings,boolean,undefined,null)
//numbers
const length = 20;
//string literal
const firstName = 'jane' // in js single quotes are used mostly
//boolean
const isTrue = true; //can only be true or false
//undefined 
const age = undefined; // if variables are not assigned they are by default undefined
//null
const gender = null; // used when explicitly clearing the value of a variable

//typeof is a function that is useful in checking of the type of a variable
console.log(typeof length)
console.log(typeof firstName)
console.log(typeof isTrue)
console.log(typeof age)
console.log(typeof gender)

//Reference types (object,arrays,functions)
//objects
//they help when working with multiple related variables 
//Maps or dictionaries in python
//they are declared using the object literal ({})

//person object
let person = {
    name:'james',
    age: 22
};
console.log(typeof person) //returns type of person

//there are two methods of accessing object properties 
// dot notation(.)

person.name = 'jane'

//bracket notation ([])

person['age'] = 18

//Arrays
//useful when storing list items
//arrays are declared using an array literal([])
//arrays are dynamic in that the size and length can change(mutable)
//arrays can contain data of different types
//arrays are objects

let 

