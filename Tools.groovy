public class Demo {
    unstash "eiffel-intelligence-artifact-wrapper"
    String message;

    Demo(String message) {
        this.message=message;
    }


    public void print(def script) {
        script.sh "echo " + message
    }
}

return new Demo();