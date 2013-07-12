# Require any additional compass plugins here.

# Set this to the root of your project when deployed:
http_path = "/"
css_dir = "css"
sass_dir = "sass"
images_dir = "images"
javascripts_dir = "js"

# You can select your preferred output style here (can be overridden via the command line):
output_style = :expanded    # :compressed
# line_comments = false

TARGET_PATH="../../../../../../tools/vagrant/mount/tomcat-7.0.40/webapps/welcome-theme/"
ACTIVE=true
watch "./**/*" do |project_dir, relative_path|
  if File.exists?(File.join(project_dir, relative_path))
    f = File.expand_path(File.join(project_dir, TARGET_PATH, relative_path))
    dir = File.dirname(f)
    unless File.exists?(dir) and File.directory?(dir)
      FileUtils.mkdir_p dir, :noop => !ACTIVE, :verbose => true
    end
    FileUtils.cp File.join(project_dir, relative_path), f, :noop => !ACTIVE, :verbose => true
  else
    FileUtils.rm f, :force => true, :noop => !ACTIVE, :verbose => true
  end
end
