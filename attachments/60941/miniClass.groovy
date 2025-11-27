def out = getBinding().out;

class Blog {
    String name
    String subject
     
    Blog() {}
     
    Blog(String name, String subject) {
        this.name = name
        this.subject = subject
    }

    def info(def out, String info) {
      out.println([info, this.name, this.subject]);
    }
}

println("Test test.");

def blog = Blog.newInstance()
blog.info(out, "Test empty");

def blog2 = Blog.newInstance(['mrhaki', 'Groovy'] as Object[])
blog2.info(out, "Test via array");
 
def blog3 = Blog.newInstance([name:'mrhaki', subject: 'Groovy'])
blog3.info(out, "Test with names");
