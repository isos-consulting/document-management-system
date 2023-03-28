const setFileName = function(fileElement) {
    const uploadForm = fileElement.closest('form');
    const fileNameElement = uploadForm.filename;

    if(fileElement.files.length === 0) {
    	fileNameElement.value = '';
    } else {
      	fileNameElement.value = fileElement.files[0].name;
    }
}

const showSuccessMessage=  async (message) => {
  alert(message);
  return true;
}

const uploadFile = async function(file) {
    	const uploadForm = file.closest('form');
    	const path = uploadForm.path;
    	const [action, uuid, parentUuid] = location.search.split('&');


    	if(location.href.includes('uuid')) {
    		const folderUuid = action.split('=')[1] === 'delete' ? parentUuid.split('=')[1] : uuid.split('=')[1];

    		const folderPathApiRes = await fetch(`/OpenKM/services/rest/folder/getPath/${folderUuid}`);
    		path.value = await folderPathApiRes.text();
    	}

    	const formData = new FormData();

    	for(const field of Array.from(uploadForm)) {
    		if(field.type === 'file') {
    		  if(uploadForm.filename.value === '') {
    			  formData.append(field.name, field.files[0]);
    		  }else {
    			  formData.append(field.name, field.files[0], uploadForm.filename.value);
    		  }
    		}else {
    			formData.append(field.name, field.value);
    		}
    	}

    	fetch('/OpenKM/frontend/FileUpload', {
    		method: "POST",
    		body: formData
    	}).then(res => {
    		if(res.ok) {
    		  showSuccessMessage('upload success').then(() => {
    		    location.href = location.href.replace('&ui-state=dialog', '');
    		  });
    		}else {
    			console.log({res});
    			fetch('https://discord.com/api/webhooks/1087966015436115968/v1i_L0FTDtLxCsI8kdJ0miNjknz2_1EHX0UK4RIVdeVVEIUKaucrikAbgbAjsospINoO', {
    				method: "POST",
    				body: JSON.stringify({
    					embeds: [
    						{
    							title: 'mobile file upload exception',
    							color: '10038562',
    							fields: [
    								{
    									name: 'message',
    									value: res
    								},
    							]
    						}
    					]
    				})
    			});
    		}
    	});
    }
