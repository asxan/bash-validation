import java.util.regex.Pattern

String nodeName = "${NODE}"
node(nodeName) 
{   
    stage("Git clone")
    {   
        sh 'echo "Executing..." '
        git branch: 'validation_bash', credentialsId: 'github-key', url: 'git@github.com:asxan/bash-validation.git'
        println "Hello"
    }
    // stage ('Sending status')
    // { 
    //     def date = new Date()
    //     println("$date")
    //     String toRecipient = "${EMAIL_ADDRESS}"
    //     String ccRecipient = ""
    //     String sSubject = "[${PROJECT_NAME}] [BASH Validation] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
    //     sh ' echo "Mail to ${toRecipient} ${ccRecipient}" '

    //     mail to: "${toRecipient}",
    //         cc: "${ccRecipient}",
    //         subject: "${sSubject}",
    //         mimeType: "text/html",
    //             body: """
    //             <head>
    //             <style type="text/css"> .simplyText{ color: "red"; font-size: 100%; } .bottomText{ font-size: 90%; font-style: "italic"} 
    //                     #form {
    //                         position: absolute;
    //                         width: 14px; 
    //                     }
    //             </style>
    //             </head>
    //             <body>
    //             <p>Dear student,</p>
    //             <p>We have received you a mail notification about validation status of bash script .</p>
    //             <pre>
    //             </pre>
    //             <p>EFS cost table:</p>
    //             <table  cellpadding="0" cellspacing="0" width="640" align="center" border="1">
    //             <thead>
    //             <tr>
    //                 <th>Service Type<br></th>
    //                 <th>General Volumes Size</th>
    //                 <th>Volume costs</th>
    //             </tr>
    //             <tr>
    //                 <td align="center">  EFS  </td>
    //                 <td align="center"> """ + efs_size_price[0] + """ GB </td>
    //                 <td align="center"> """ + efs_size_price[2] + """  \$</td>
    //             </tr>
    //             </thead>
    //             </table>
    //             <table  cellpadding="0" cellspacing="0" width="640" align="center" border="1">
    //             <thead>
    //             <tr>
    //                 <th>EDP usage</th>
    //                 <th>Total price</th>
    //             </tr>
    //             <tr>
    //                 <td align="center"> """ + efs_size_price[3] + """  \$</td>
    //                 <td align="center"> """ + efs_size_price[1] + """  \$</td>
    //             </tr>
    //             </thead>
    //             </table>
    //             <pre>
    //             </pre>
    //             <p>EFS table by namespaces:</p>
    //             <table  cellpadding="0" cellspacing="0" width="920" align="center" border="1">
    //             <thead>
    //                 """ + efs_volumes + """
    //             </thead>
    //             </table>
    //             <p>Best regards,</p>
    //             <p>EDP Support Team</p>
    //             <p class="bottomText">This is an automatically generated email – please do not reply to it. If you have any questions, please email SupportEPMD-EDP@epam.com.</p>
    //             </body>
    //             """
    // } 
}