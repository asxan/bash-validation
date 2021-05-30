import java.util.regex.Pattern


def mail_datas(String toRecipient, String ccRecipient, String project_name)
{   
    def final_list = []
    def date = new Date()
    println("$date")
    final_list[0] = toRecipient
    final_list[1] = ccRecipient
    final_list[2] = "[${project_name}] [BASH Validation] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"

    return final_list
}


def parse_output_for_mail(String text)
{   
    def lines = "" 
    for (i in text.split(",")) 
    {
        lines = lines + "<p>" + "$i" + "\n" +"</p>" 
    }
    println("$lines")

    return lines
}


def mail_notification(String toRecipient, String ccRecipient, String sSubject, String message1, String message2, String message3)
{
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
            <p>We have sent you a mail notification about validation status of bash scripts.</p>
            <pre>
            </pre>
            <p>Json format of result your first scripts:</p>              
            <p>"""+ message1 +"""</p>
            <pre>
            </pre>
            <p>Json format of result your second scripts:</p>              
            <p>"""+ message2 +"""</p>
            <pre>
            </pre>
            <p>Json format of result your third scripts:</p>              
            <p>"""+ message3 +"""</p>
            <pre>
            </pre>
            <p>Best regards,</p>
            <p>Support command of bash validation service</p>
            <p class="bottomText">This is an automatically generated email – please do not reply to it.</p>
            </body>
            """
}


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
                }
            }
            dir('python_script')
            {
                script
                {
                    git branch: 'python_script', credentialsId: 'github-key', url: 'git@github.com:asxan/bash-validation.git'
                }
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
    try
    {
        stage("Execute script")
        {   
            script
            {
                dir("gold_solution")
                {
                    sh( script: 'bash ./scripts/first_task.sh input_data/example.log',  returnStdout: true ).trim()
                    sh( script: 'bash ./scripts/second_task.sh --all',  returnStdout: true ).trim()
                    sh(script: "bash ./scripts/excute_second_script.sh scripts/second_task.sh output/ip_addrs.txt", returnStdout: true).trim()
                }
                dir("student_solution")
                {
                    sh(script: """bash ./scripts/first_task.sh input_data/example.log""", returnStdout: true).trim()
                    sh( script: 'bash ./scripts/second_task.sh --all',  returnStdout: true ).trim()
                    sh(script: "bash ./scripts/excute_second_script.sh scripts/second_task.sh output/ip_addrs.txt", returnStdout: true).trim()
                }
                dir("python_script")
                {
                    sh 'ls -la'
                    sh '''#!/bin/bash
                        virtualenv venv
                        source venv/bin/activate
                        pip3 install -r requirements.txt
                        pip3 freeze
                        python3 -m xmlrunner -o junit-reports testFirstTask.py
                        python3 xml_parser.py junit-reports/"$(ls -1 junit-reports)" 1_file.json
                        ls -la junit-reports
                        rm -rf junit-reports/*
                        python3 -m xmlrunner -o junit-reports testSecondTask.py
                        python3 xml_parser.py junit-reports/"$(ls -1 junit-reports)" 2_file.json
                        python3 json_parser.py 1_file.json 1_result.txt
                        python3 json_parser.py 2_file.json 2_result.txt
                        ls -la
                    '''
                }
            }
        }
        stage ('Sending status')
        {
            script
            {
                def date = new Date()
                println("$date")
                String toRecipient = "${EMAIL_ADDRESS}"
                String ccRecipient = ""
                String sSubject = "[${PROJECT_NAME}] [EFS cost status] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
                parse_message1 = parse_output_for_mail(sh( script: 'cat python_script/1_result.txt',  returnStdout: true ).trim())
                parse_message2 = parse_output_for_mail(sh( script: 'cat python_script/2_result.txt',  returnStdout: true ).trim())
                parse_message3 = ""
                
                sh 'echo "Mail to ${toRecipient} ${ccRecipient}"' 
                mail_notification(toRecipient, ccRecipient, sSubject, parse_message1, parse_message2, parse_message3)
            } 
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
            def date = new Date()
            println("$date")
            String toRecipient = "${EMAIL_ADDRESS}"
            String ccRecipient = ""
            String sSubject = "[${PROJECT_NAME}] [EFS cost status] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
            parse_message1 = parse_output_for_mail(sh( script: 'cat python_script/1_result.txt',  returnStdout: true ).trim())
            parse_message2 = parse_output_for_mail(sh( script: 'cat python_script/2_result.txt',  returnStdout: true ).trim())
            parse_message3 = ""
            
            sh 'echo "Mail to ${toRecipient} ${ccRecipient}"' 
            mail_notification(toRecipient, ccRecipient, sSubject, parse_message1, parse_message2, parse_message3)
            sh """
            rm -rf *
            rm -rf .git
            ls -la
            """
        }
    }  
}