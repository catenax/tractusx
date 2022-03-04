import { ComponentStory } from '@storybook/react'

import { Radio as Component } from '.'

export default {
  title: 'Form',
  component: Component,
  argTypes: {
    onClick: {
      action: 'onClick',
    },
  },
}

const Template: ComponentStory<typeof Component> = (args: any) => (
  <Component {...args} />
)

export const MuiRadio = Template.bind({})
MuiRadio.args = {
  disabled: true,
}
