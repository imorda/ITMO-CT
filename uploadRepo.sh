HEADER_ACCEPT='Content-Type: application/json'
HEADER_CONTENT_TYPE='Content-Type: application/json'
GITHUB_API_KEY=''
GITHUB_BASE_URL='https://api.github.com/'

STARTDIR=$(pwd)

for x in $(find . -type d) ; do
    if [ -d "${x}/.git" ] ; then
        cd "${x}"
        
        cd ..
        COURSE_NAME=$(basename "$PWD")
        cd $STARTDIR
        cd "${x}"

        PROJECT_NAME=$COURSE_NAME"-"$(basename "$PWD")
        SETUP_GITHUB_PROJECT_JSON='{
            "name": "'$PROJECT_NAME'",
            "description": "A public version of a CT-ITMO course repository"
        }'

        echo "Creating GitHub repo for project "$PROJECT_NAME"..."
        curl -X POST -d "$SETUP_GITHUB_PROJECT_JSON" -H "$HEADER_ACCEPT" -H "$HEADER_CONTENT_TYPE" -u $GITHUB_API_KEY:x-oauth-basic "$GITHUB_BASE_URL"user/repos >/dev/null

        echo Adding git remote
        git remote add public "git@github.com:imorda/"$PROJECT_NAME".git"
        echo Pushing to GitHub...
        git push public

        origin="$(git config --get remote.public.url)"
        cd - 1>/dev/null
        echo Adding a submodule...
        git submodule add "${origin}" "${x}"
        echo ""
        echo ""
    fi
done
