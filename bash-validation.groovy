import java.util.regex.Pattern

String nodeName = "${NODE}"
node(nodeName) 
{   try
    {
        stage("Git clone") ///home/asxan_devops/jenkins
        {   
            sh 'echo "Executing..." '
            dir("gold_solution")
            {   script
                {
                    git branch: 'validation_bash', credentialsId: 'github-key', url: 'git@github.com:asxan/bash-validation.git'
                }
            }
            dir('student_solution')
            {
                script
                {
                    git branch: 'master', url: "${HTTPS_CLONE_STUDENT_REPO}"
                    sh 'ls -la'
                }
            }
        }
        stage("Execute script")
        {
            script
            {
                dir("gold_solution")
                {
                    sh( script: 'bash ./scripts/first_task.sh input_data/example.log',  returnStdout: true ).trim()
                } 
            }
        }
        stage ('Sending status')
        { 
            def date = new Date()
            println("$date")
            String toRecipient = "${EMAIL_ADDRESS}"
            String ccRecipient = ""
            String sSubject = "[${PROJECT_NAME}] [BASH Validation] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
            sh ' echo "Mail to ${toRecipient} ${ccRecipient}" '

            mail to: "${toRecipient}",
                cc: "${ccRecipient}",
                subject: "${sSubject}",
                mimeType: "text/html",
                    body: """
                    <head>
                    <style type="text/css"> .simplyText{ color: "red"; font-size: 100%; } .bottomText{ font-size: 90%; font-style: "italic"} 
                            #form {
                                position: absolute;
                                width: 14px; 
                            }
                    </style>
                    </head>
                    <body>
                    <p>Dear student,</p>
                    <p>We have received you a mail notification about validation status of bash script .</p>
                    <pre>
                    </pre>
                    <p>EFS cost table:</p>              
                    <pre>
                    </pre>
                    <p>EFS table by namespaces:</p>
                    <p>Best regards,</p>
                    <p>EDP Support Team</p>
                    <p class="bottomText">This is an automatically generated email – please do not reply to it. If you have any questions, please email SupportEPMD-EDP@epam.com.</p>
                    </body>
                    """
        }
        stage('Clear workdir')
        {
            script
            {
                sh """
                rm -rf *
                rm -rf .git
                ls -la
            """
            }
        }
    }
    catch(Exception ex)
    {
        script{
            sh """
            rm -rf *
            rm -rf .git
            ls -la
            """
        }
    }  
}