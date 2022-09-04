 $(document).ready(function() {
            var barColors = ["#f5351e", "#89fdd4", "#d6224b", "#a3263d", "#d8af5a", "#690cfd", "#cdda68",
                "#d85444", "#67d7cf", "#735230", "#e8fdf4", "#f146dc", "#2d6382", "#a6eb16",
                "#5823a4", "#d971b6", "#89f73a", "#5b3209", "#68ae06", "#ae77f9", "#84cc87",
                "#ac6eee", "#025532", "#3533dd", "#28c439", "#e6535b", "#8ff8e2", "#c0befd"
            ];

			
			
            const random1 = Math.floor(Math.random() * barColors.length);
            new Chart(document.getElementById("doughnut-chart-student"), {
                type: 'doughnut',
                data: {
                    labels: getData("femaleMaleRatioStudent")[0],
                    datasets: [{
                        label: "Population (millions)",
                        backgroundColor: barColors.slice(random1, random1 + 2),
                        data: getData("femaleMaleRatioStudent")[1]
                    }]
                },
                options: {
                    layout: {
                        padding: 1
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Students Female-Male Ratio'
                        }
                    }
                }
            });
            
            const random2 = Math.floor(Math.random() * barColors.length);
            new Chart(document.getElementById("doughnut-chart-instructor"), {
                type: 'doughnut',
                data: {
                    labels: getData("femaleMaleRatioInstructor")[0],
                    datasets: [{
                        label: "Population (millions)",
                        backgroundColor: barColors.slice(random2, random2 + 2),
                        data: getData("femaleMaleRatioInstructor")[1]
                    }]
                },
                options: {
                    layout: {
                        padding: 1
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: 'Instructors Female-Male Ratio'
                        }
                    }
                }
            });



            const random3 = Math.floor(Math.random() * barColors.length);
            new Chart(document.getElementById("pie-chart"), {
                type: 'pie',
                data: {
                    labels: getData("departmentsCourses")[0],
                    datasets: [{
                        label: "Population (millions)",
                        backgroundColor: barColors.slice(random3, random3 + 5),
                        data: getData("departmentsCourses")[1]
                    }]
                },
                options: {
                    layout: {
                        padding: 1
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: "Departments' Courses"
                        }
                    }
                }
            });

            const random4 = Math.floor(Math.random() * barColors.length);
            new Chart(document.getElementById("bar-chart-horizontal-city"), {
                type: 'bar',
                indexAxis: 'y',
                data: {
                    labels: getData("citiesProportions")[0],
                    datasets: [{
                        label: "Count",
                        backgroundColor: barColors.slice(random4, random4 + 5),
                        data: getData("citiesProportions")[1]
                    }]
                },
                options: {
                    layout: {
                        padding: 30
                    },
                    legend: {
                        display: false
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: "Cities proportions"
                        }
                    }
                }
            });
            
            const random5 = Math.floor(Math.random() * barColors.length);
            new Chart(document.getElementById("bar-chart-horizontal-course"), {
                type: 'bar',
                indexAxis: 'y',
                data: {
                    labels: getData("courseProportions")[0],
                    datasets: [{
                        label: "Count",
                        backgroundColor: barColors.slice(random5, random5 + 5),
                        data: getData("courseProportions")[1]
                    }]
                },
                options: {
                    layout: {
                        padding: 30
                    },
                    legend: {
                        display: false
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: "Courses proportions"
                        }
                    }
                }
            });




		function getData(elementId){
			var femaleMaleMap = document.getElementById(elementId).value;

			var femaleMaleList = femaleMaleMap.slice(femaleMaleMap.indexOf("[", 0)+1, femaleMaleMap.indexOf("]", 0));
			var femaleMaleArray = femaleMaleList.split(",");
			
			var splitterArr1 = [];
			var keyArr = [];
			var valueArr = [];
			femaleMaleArray.forEach(myFunction1);
			function myFunction1(item, index, arr) {
			  splitterArr1[index] = arr[index].split("=");
			}
			splitterArr1.forEach(fun2);
			function fun2(item, index, arr) {
			    keyArr[index] = item[0];
			    valueArr[index] = item[1];
			}
			var resultArray = [keyArr, valueArr];
			return resultArray;
		}



        });