//This file will be stored in S3 bucket
//Registering both the helpers

Handlebars.registerHelper('ifCond', function(v1, options) {
            if(v1 >= 18) {
                return options.fn(this);
            }
            return options.inverse(this);
        });

Handlebars.registerHelper('checkGender', function(value){
	var retVal = '';
    if(value === 'M') {
        retVal =  'Male.';
    } else if (value === 'F') {
        retVal = 'Female.'
    } else {
        retVal = '';
    }
    return new Handlebars.SafeString(retVal);
});