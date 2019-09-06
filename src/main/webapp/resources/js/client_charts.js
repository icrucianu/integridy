var realDataPointsMap = new Map();
var realLabels = [];

function getRealData(realDataJSON, canvasName){
//    console.log("realdata data" + realDataJSON );
    console.log("realdata data " + canvasName );
    if (typeof realDataJSON === 'undefined' ) {
       return;
    }
    let data = JSON.parse(realDataJSON);
    if(data.length === 0)
        return;
    var realDataPoints = [];
    var dateFormat = 'YYYY-MM-DD HH:mm:ss';
    for(var key in data){
        if(data.hasOwnProperty(key)){
            
            let datetime = moment(data[key].time, dateFormat);
           
            let dataPoint = {
                x: datetime ,
                y: data[key].value
            };
            
            realDataPoints.push(dataPoint);
            realLabels.push(data[key].time);
            
        }
    }
    realDataPointsMap.set(canvasName, realDataPoints);
}
function getOptimumData(optimumDataJSON, canvasName){
//    console.log("Optimum data" + optimumDataJSON );
    
    console.log("Optimum data" + canvasName );
    
    if (typeof optimumDataJSON === 'undefined') {
       return;
    }
    let optimumData = JSON.parse(optimumDataJSON);
    if(optimumData.length === 0)
        return;
    let dataPoints = [];
    let labels = [];
    var dateFormat = 'YYYY-MM-DD HH:mm:ss';
    for(var key in optimumData){
        if(optimumData.hasOwnProperty(key)){
            
            let datetime = moment(optimumData[key].time, dateFormat);
           
            let dataPoint = {
                x: datetime ,
                y: optimumData[key].value
            };
            
            dataPoints.push(dataPoint);
            labels.push(optimumData[key].time);
            
        }
    }
    
    var ctx = document.getElementById(canvasName).getContext('2d');
		ctx.canvas.width = 800;
		ctx.canvas.height = 200 
		var cfg = {
			type: 'line',
			data: {
				labels: labels,
				datasets: [{
					label: 'Optimum consumption',
					data: dataPoints,
					type: 'line',
                                        fill: false,
                                        borderDash: [5,5],
                                        borderColor: window.chartColors.red
					
				},
                            {
					label: 'Real consumption',
					data: realDataPointsMap.get(canvasName),
					type: 'line',
					fill: false,
                                         borderDash: [5,5],
                                        pointRadius: 0,
                                        lineTension: 0,
                                        borderColor: window.chartColors.blue
					
				}]
			},
			options: {
				scales: {
					xAxes: [{
						type: 'time',
						distribution: 'linear',
						ticks: {
							source: 'labels'
						}
					}],
					yAxes: [{
						scaleLabel: {
							display: true,
							labelString: 'Optimum consumption'
						}
					}]
				}
			}
		};
		var chart = new Chart(ctx, cfg);
    
}


function getForecastData(forecastDataJSON, canvasName){
//    console.log("Optimum data" + optimumDataJSON );
    
    console.log("Optimum data" + canvasName );
    
    if (typeof forecastDataJSON === 'undefined') {
       return;
    }
    let forecastData = JSON.parse(forecastDataJSON);
    if(forecastData.length === 0)
        return;
    let dataPoints = [];
    let labels = [];
    var dateFormat = 'YYYY-MM-DD HH:mm:ss';
    for(var key in forecastData){
        if(forecastData.hasOwnProperty(key)){
            
            let datetime = moment(forecastData[key].time, dateFormat);
           
            let dataPoint = {
                x: datetime ,
                y: forecastData[key].value
            };
            
            dataPoints.push(dataPoint);
            labels.push(forecastData[key].time);
            
        }
    }
    
    var ctx = document.getElementById(canvasName).getContext('2d');
		ctx.canvas.width = 800;
		ctx.canvas.height = 200 
		var cfg = {
			type: 'line',
			data: {
				labels: labels,
				datasets: [{
					label: 'Forecast consumption',
					data: dataPoints,
					type: 'line',
					fill: false,
                                        pointRadius: 3,
                                        lineTension: 0.2,
                                         borderDash: [5,5],
                                        borderColor: window.chartColors.green
					
				}]
			},
			options: {
				scales: {
					xAxes: [{
						type: 'time',
						distribution: 'linear',
						ticks: {
							source: 'labels'
						}
					}],
					yAxes: [{
						scaleLabel: {
							display: true,
							labelString: 'Forecast consumption'
						}
					}]
				}
			}
		};
		var chart = new Chart(ctx, cfg);
    
}

		