import { ActionButton, DefaultButton, Dialog, DialogFooter, DialogType, IIconProps, PrimaryButton } from '@fluentui/react';
import { useBoolean } from '@fluentui/react-hooks'
import { useState } from 'react';
import { useHistory } from 'react-router';
import { deleteModel } from './data';

export default function DeleteModel(props){
  const [error, setError] = useState(null);
  const [hideDeleteDialog, {toggle: toggleHideDeleteDialog}] = useBoolean(true);
  const [hideErrorDialog, {toggle: toggleHideErrorDialog}] = useBoolean(true);
  const deleteIcon: IIconProps = { iconName: 'Delete' };
  const history = useHistory();

  const modelProps = {
    isBlocking: false,
    styles: { main: { maxWidth: 800 } },
  };

  const deleteDialogContentProps = {
    type: DialogType.largeHeader,
    title: 'Delete Model',
    subText:'You are about to delete the current model:'
  };

  const errorDialogContentProps = {
    type: DialogType.largeHeader,
    title: 'Error',
    subText: error
  };

  const requestDeletion = () => {
    setError(null);
    toggleHideDeleteDialog();
  }

  const confirmDeletion = () => {
    deleteModel(props.id).then(
      data => {
        history.push('/home/semantichub/');
      },
      error => {
        setError(error.message);
        toggleHideDeleteDialog();
        toggleHideErrorDialog();
      }
    )
  }

  return(
    <>
      <ActionButton iconProps={deleteIcon} onClick={requestDeletion}>Delete model</ActionButton>
      <Dialog
        hidden={hideDeleteDialog}
        onDismiss={toggleHideDeleteDialog}
        dialogContentProps={deleteDialogContentProps}
        modalProps={modelProps}
      >
        <p><span>Name: </span>{props.name}</p>
        <p className="bw"><span>Version: </span>{props.version}</p>
        <p className="mt20">Do you want to continue?</p>
        <DialogFooter>
          <PrimaryButton onClick={confirmDeletion} text="Continue" />
          <DefaultButton onClick={toggleHideDeleteDialog} text="Cancel" />
        </DialogFooter>
      </Dialog>
      <Dialog
        hidden={hideErrorDialog}
        onDismiss={toggleHideErrorDialog}
        dialogContentProps={errorDialogContentProps}
        modalProps={modelProps}
      >
        <DialogFooter>
          <DefaultButton onClick={toggleHideErrorDialog} text="OK" />
        </DialogFooter>
      </Dialog>
    </>
  )
}
