// To load a module into our current module use the method require('path_of_module')
// Require function returns the method that is exported from the specified module
let http = require('http')


// Print hello word to the console
console.log('Hello World');

// Node is a runtime environment based on the google chrome v8 javascript runtime environment
// Every file in a node app is considered a module
// This is to avoid functions with similar names overwriting each other in the global scope
// Every function and variable defined has a scope of the module unless it is exported
// Returns the id and other module info (module properties)
console.log(module);
// Exports are the module functions and variables declared in the module explicitly as exported (Public in oop)
// Exported functions and variables can be used outside their scope

console.log(http)