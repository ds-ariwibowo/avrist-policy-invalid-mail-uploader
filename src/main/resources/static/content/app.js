(function (){

var app = angular.module('MyApp', []);

app.controller('MyController', ['$scope', '$http', '$window', function ($scope, $http, $window) {

    $("#mstId").val("");
    $scope.Msg = ""
    $scope.RowCount = 0;
    $scope.Success = 0;
    $scope.Failed = 0;
    $scope.SelectedFileForUpload = null;
    
    $scope.UploadFile = function (files) {       
        $scope.$apply(function () { //I have used $scope.$apply because I will call this function from File input type control which is not supported 2 way binding
            $scope.Message = "";
            $scope.Warning = "";
            $scope.SelectedFileForUpload = files[0];
        })              
    }

    //Reset
    $scope.RefreshPage = function () {           
        window.location.reload();           
    }

    //Clear
    $scope.Clear = function () {           
        $scope.RowCount = 0;
        $scope.Success = 0;
        $scope.Failed = 0;
        $scope.SelectedFileForUpload = null;              
    }

    //Parse Excel Data 
    $scope.ParseExcelDataAndSave = function () {   
        $scope.Message = "";
        $scope.Warning = "";
        var data;
        var byteString;
        var filedata;
        var mimeString;

        var file = $scope.SelectedFileForUpload;
        if (file) {                      
            debugger
            
            var reader = new FileReader();                                  
            reader.onload = function (e) {               
                data = e.target.result;
                if (data.split(',')[0].indexOf('base64') >= 0) {
                    byteString = atob(data.split(',')[1]);
                } else {
                    byteString = decodeURI(data.split(',')[1]);
                }
                filedata =data.split(',')[1];
                mimeString = data.split(',')[0].split(':')[1].split(';')[0];
                
                
                if (mimeString === "application/vnd.ms-excel")
                {  
                    var rows = byteString.split("\n");
                    if (rows.length <= 2 || (rows[1].split(",")[0].toString() == "") )
                    {
                        $scope.Msg = "File is empty !";
                    }
                    else
                    {                        
                        var obj = {fileName:"",uploadBy:"",data:"",recovery:"N"}
                        obj.fileName = file.name;
                        obj.uploadBy = "Dwi Satryo Ariwibowo"; 
                        //workbook.Props.LastAuthor.toString();
                        obj.data = filedata;
                        $scope.UploadingFile(obj, byteString)                    
                    }                   
                }
                else {
                    $scope.Msg = "Please using CSV file only !";
                }        
                $scope.Warning = $scope.Msg;  
            }
           
            reader.onerror = function (ex) {
                console.log(ex);
            }
            reader.readAsDataURL(file); 
                     
            //reader.readAsBinaryString(file);
            $scope.Clear();    
            $("#file").val("");
        }
        else 
        {
            //alert("File upload not found !");            
            $scope.Warning = "File upload not found !";
        }        
    }

    // $scope.dataURItoBlob = function(dataURI) {
    //     // convert base64/URLEncoded data component to raw binary data held in a string
    //     var byteString;
    //     if (dataURI.split(',')[0].indexOf('base64') >= 0) {
    //       byteString = atob(dataURI.split(',')[1]);
    //     } else {
    //       byteString = decodeURI(dataURI.split(',')[1]);
    //     }
    //     var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    //     var array = [];
    //     for(var i = 0; i < byteString.length; i++) {
    //       array.push(byteString.charCodeAt(i));
    //     }
    //     return new Blob([new Uint8Array(array)], {type: mimeString});
    //   }

    $scope.submit = function () {
        
        document.getElementById("overlay").style.display = "block";             
        $http({
           method: 'POST',
           url: "/api/updateAddress/"+ $("#mstId").val(),
           headers: {
               'Content-Type' : 'application/json'
           }
           }).then(function (response) {
               if (response.status) {
            }
            else 
            {
                $scope.Warning = "Submission data failed !";
            }
            document.getElementById("overlay").style.display = "none";  
            },
            function (error) {
               $scope.Warning = "Submission data failed !";
               document.getElementById("overlay").style.display = "none";
            })       

    }

    $scope.UploadingFile = function (excelfile, byteString) {

         document.getElementById("overlay").style.display = "block";             
         $http({
            method: 'POST',
            url: "/api/masters",
            data: JSON.stringify(excelfile),
            headers: {
                'Content-Type' : 'application/json' //'Content-Type': 'multipart/form-data'
            }
            }).then(function (response) {
                if (response.status) {
                    $("#mstId").val(response.data.id);
                    
                    // insert data  
                    var pCode=[]
                    var rows = byteString.split("\n");                 
                    for (var i = 1; i < rows.length; i++) {
                        $scope.RowCount = i - 1;
                        var cells = rows[i].split(",");
                        if (cells.length == 4)
                        {
                            var objDetail = {policyCode:"",letter:"",reason:"",effectiveDate:"",counter:0} 
                            objDetail.policyCode = cells[0].toString().trim();  
                            objDetail.letter = cells[1].toString().trim();  
                            objDetail.reason = cells[2].toString().trim();  
                            objDetail.effectiveDate = cells[3].toString().trim();

                            pCode.push(objDetail.policyCode);                            
                            var counter = 0;
                            for (var i = 0; i < pCode.length; i++) {
                                if (pCode[i]==objDetail.policyCode)
                                    counter+=1;                               
                            }
                            objDetail.counter = counter;
                            //$scope.UpdateData(objDetail) // call api directly below
                            
                            $http({
                                method: "POST",
                                url: "/api/masters/" + response.data.id + "/details/" + objDetail.policyCode,
                                data: JSON.stringify(objDetail),
                                headers: {
                                    'Content-Type' : 'application/json' //'Content-Type': 'multipart/form-data'
                                }
                            }).then(function (data) {
                                if (data.status) {
                                    $scope.Success += 1;   
                                    $('#MyTable tbody:last-child').append("<tr><td><b>"+data.data.policyCode+"</b></td><td>"+data.data.letter+"</td><td>"+data.data.reason+"</td><td>"+data.data.effectiveDate+"</td><td><font color='forestgreen'><b>Success</b></font></td></tr>")   
                                }
                                else {
                                    $scope.Failed += 1;
                                    $('#MyTable tbody:last-child').append("<tr><td><b>"+data.data.policyCode+"</b></td><td>"+data.data.letter+"</td><td>"+data.data.reason+"</td><td>"+data.data.effectiveDate+"</td><td><font color='red'><b>Failed</b></font></td></tr>")   
                                    
                                }
                                $scope.Message =  "File already uploaded with Result : "+$scope.Success + " records submitted";
                                if ($scope.Failed >0)
                                    $scope.Message += " and " + $scope.Failed + " records failed";
                                $scope.Message += " (total "+$scope.RowCount+" records)";
                                document.getElementById("overlay").style.display = "none";
                    
                            }, function (error) {
                                $scope.Warning = "Data can not continue to process, uncorrectly format ! ";
                                
                                //restore data
                                $http({
                                    method: 'DELETE',
                                    url: "/api/masters/" + response.data.id ,                                    
                                    headers: {
                                        'Content-Type' : 'application/json' //'Content-Type': 'multipart/form-data'
                                    }
                                    }).then(function (response) {
                                        $scope.Warning += ", Data file " + response.data.fileName + "already rollback !"; 
                                    })
                                document.getElementById("overlay").style.display = "none";
                            }) 
                        }
                    }                   
                    // insert finish
                }
                else 
                {
                    $scope.Warning = "Failed to proceed file !";
                }
                document.getElementById("overlay").style.display = "none";  
            },
            function (error) {
           
            if (error.data.message.includes("UK_gql9imgptqxm40i2n3mxpldor"))
                $scope.Warning = "Data file "+excelfile.fileName +" already exist !";
            else
                $scope.Warning = "Upload file failed !";

            document.getElementById("overlay").style.display = "none";
            })        
                      
     
        //   let a = new FormData();
        //   a.append("file",file);
        //   a.append("uploadBy",author) 
        //   $http({
        //     method: 'POST',
        //     type: 'POST',
        //     enctype: 'multipart/form-data',
        //     url: "/api/uploadFile",
        //     data: a,
        //     processData: false, //prevent jQuery from automatically transforming the data into a query string
        //     contentType: 'multipart/form-data',          
        //     cache: false,
        //     timeout: 600000
        //     }).then(function (response) {
        //         if (response.status) {
        //             $scope.Message = "Upload success !";
        //         }
        //         else 
        //         {
        //             $scope.Message = "Upload Failed !";
        //         }
        //     },
        //     function (error) {
        //     $scope.Message = "Error Upload file !";
        //     })
    }   

    // Download excel data - not used
    // $http({
    //     method: "POST",
    //     url: "/api/notes",
    //     data: JSON.stringify(excelData),
    //     headers: {
    //         'Content-Type' : 'application/json'
    //     }   
    // $scope.export = function () { 
    //     //$http.post('/table/export.do', req,{responseType: 'arraybuffer'}
    //     $http({
    //         method: "GET",
    //         url: "/api/policy",            
    //         headers: {
    //             'Content-Type' : 'application/json'
    //             }
    //         }).then(function (response) {
    //             debugger
    //             var header = "test"//response.headers('Content-Disposition')
    //             var fileName = "test.csv" //header.split("=")[1].replace(/\"/gi,'');
    //             console.log(fileName); 
    //             var blob = new Blob([response.data],
    //                 //{type : 'application/vnd.openxmlformats-officedocument.presentationml.presentation;charset=UTF-8'}
    //                 {type : 'text/csv'}
    //             );
    //             var objectUrl = (window.URL || window.webkitURL).createObjectURL(blob);
    //             var link = angular.element('<a/>');
    //             link.attr({
    //                 href : objectUrl,
    //                 download : fileName
    //             })[0].click();
    //         })
    // }
  

     // Download csv data
    $scope.saveFile = function () {
        var a = document.createElement("a");
        var json_pre = '[{"POLICY_CODE":"","LETTER":"","REASON":"","EFFECTIVE_DATE":""}]'
        
        var csv = Papa.unparse(json_pre);
   
        if (window.navigator.msSaveOrOpenBlob) {
          var blob = new Blob([decodeURIComponent(encodeURI(csv))], {
            type: "text/csv;charset=utf-8;"
          });
          navigator.msSaveBlob(blob, 'Template_Upload.csv');
        } else {
   
          a.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(csv);
          a.target = '_blank';
          a.download = 'Template_Upload.csv';
          document.body.appendChild(a);
          a.click();
        }
      }

    // // Download excel data
    // $scope.saveFile = function () {
    //     var sheet_1_data = [{POLICY_CODE:"", LETTER:"", REASON:"", EFFECTIVE_DATE:""}];       
    //     var opts = [{sheetid:'List of invalid policy address',header:true}];
    //     var result = alasql('SELECT * INTO XLSX("Template_Upload.xls",?) FROM ?', 
    //                                         [opts,[sheet_1_data]]);
    // }
   
    }])

})();