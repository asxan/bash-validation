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


def mail_notification(String toRecipient, String ccRecipient, String sSubject, String message)
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
            <pre>
            </pre>
            <p>"""+ message +"""</p>
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
                    // sh(script: """
                    // while read line; do
                    // ./scripts/second_task.sh --target="$line"
                    // done < output/ip_addrs.txt
                    // """, returnStdout: true ).trim()
                    sh(script: "./script/excute_second_script.sh script/second_script.sh output/ip_addrs.txt", returnStdout: true).trim()
                    //sh(script: "./scripts/second_task.sh --target=10.0.0.4", returnStdout: true ).trim()

                    //sh """while read line; do ./scripts/second_task.sh --target=$line; done < output/ip_addrs.txt"""
                    sh "ls -la output"
                    sh "cat output/ip_addrs.txt"
                    //sh "cat output/tcp_ports.txt"
                    sh 'pwd'
                }
                // dir("student_solution")
                // {
                //     sh 'mkdir output'
                //     sh(script: """bash ./scripts/first_task.sh input_data/example.log""", returnStdout: true).trim()
                //     sh "ls -la output"
                //     sh 'pwd'
                // }
                // dir("python_script")
                // {
                //     sh 'ls -la'
                //     sh '''#!/bin/bash
                //         virtualenv venv
                //         source venv/bin/activate
                //         pip3 install -r requirements.txt
                //         pip3 freeze
                //         python3 -m xmlrunner -o junit-reports testFirstTask.py
                //         python3 xml_parser.py junit-reports/"$(ls -1 junit-reports)" 1_file.json
                //         python3 json_parser.py *.json
                //         ls -la
                //     '''
                // }
            }
        }
        stage ('Sending status')
        {
            script
            {
                def date = new Date()
                println("$date")
                // String toRecipient = "${EMAIL_ADDRESS}"
                // String ccRecipient = ""
                // String sSubject = "[${PROJECT_NAME}] [EFS cost status] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
                // message = sh( script: 'cat python_script/result_json_parse.*',  returnStdout: true ).trim()

                // parse_message = parse_output_for_mail(message)
                // sh ' echo "Mail to ${toRecipient} ${ccRecipient}" ' 
                // mail_notification(toRecipient, ccRecipient, sSubject, parse_message)
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
            // String toRecipient = "${EMAIL_ADDRESS}"
            // String ccRecipient = ""
            // String sSubject = "[${PROJECT_NAME}] [EFS cost status] [${date.format("yyyy-MM-dd HH:mm:ss")} UTC]"
            // message = sh( script: 'cat python_script/result_json_parse.*',  returnStdout: true ).trim()

            // parse_message = parse_output_for_mail(message)
            // sh ' echo "Mail to ${toRecipient} ${ccRecipient}" ' 
            // mail_notification(toRecipient, ccRecipient, sSubject, parse_message)
            sh """
            rm -rf *
            rm -rf .git
            ls -la
            """
        }
    }  
}