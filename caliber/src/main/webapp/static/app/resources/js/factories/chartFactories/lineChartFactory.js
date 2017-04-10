angular
		.module("api")
		.factory(
				"lineChartFactory",
				function($http, $log) {
					$log.debug("Booted Report Factory");

					var report = {};

					/**
					 * Yanilda
					 */
					report.getTraineeUpToWeekLineChart = function(week, traineeId) {
						return $http(
								{
									url : "/all/reports/batch/week/" + week
											+ "/trainee/" + traineeId + "/line-trainee-up-to-week",
									method : "GET"
								})
								.then(
										function(response) {
											$log
													.debug("Agg - Batch - week-- trainee -- success");
											$log.debug(response);
											return response.data;
										},
										function(response) {
											$log.error("There was an error: "
													+ response.status);
										});
					};
					
					
					
					
					
					
				})
